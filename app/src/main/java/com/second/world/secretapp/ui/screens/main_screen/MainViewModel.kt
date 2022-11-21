package com.second.world.secretapp.ui.screens.main_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.second.world.secretapp.core.bases.BaseResult
import com.second.world.secretapp.core.bases.BaseViewModel
import com.second.world.secretapp.core.bases.Dispatchers
import com.second.world.secretapp.core.extension.log
import com.second.world.secretapp.core.extension.newListIo
import com.second.world.secretapp.core.remote.Failure
import com.second.world.secretapp.core.remote.ResponseWrapper
import com.second.world.secretapp.data.app.local.AppPrefs
import com.second.world.secretapp.data.main_screen.remote.common.model.response.ResponseMainScreen
import com.second.world.secretapp.data.main_screen.remote.conn_elements.services.ServiceConnectionItem
import com.second.world.secretapp.data.main_screen.repository.MainScreenRepository
import com.second.world.secretapp.domain.main_screen.ConnectionUseCase
import com.second.world.secretapp.ui.screens.main_screen.model_ui.MapperConnDataToUI
import com.second.world.secretapp.ui.screens.main_screen.model_ui.SrvItemUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import okhttp3.OkHttpClient
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val appPrefs: AppPrefs,
    private val dispatchers: Dispatchers,
    private val repositoryMain: MainScreenRepository,
    private val connMapper: MapperConnDataToUI,
    private val connUseCase: ConnectionUseCase,
    private val responseWrapper: ResponseWrapper,
    private val okHttpClient: OkHttpClient
) : BaseViewModel() {

    private val _mainScreenState = MutableLiveData<MainScreenState>()
    val mainScreenState: LiveData<MainScreenState> = _mainScreenState

    private val _listConn = MutableLiveData<List<SrvItemUi?>?>()
    val listConn: LiveData<List<SrvItemUi?>?> = _listConn

    init {
        getMainScreenUi()
    }

    /**
     * 1. Сперва получить весь список КНОПОК, смапить их в другой тип данных где нужно добавить id элементам
     * 2. Записать кнопки в лив дату
     * 3. После записи вызвать метод, где в цикле будут вызываться запросы для каждого элемента
     * 4. Для каждого элемента получить данные и по id заменить данные
     */

    private fun getMainScreenUi() {

        _mainScreenState.value = MainScreenState.Loading

        dispatchers.launchBackground(viewModelScope) {

            val result: BaseResult<ResponseMainScreen, Failure> =
                repositoryMain.getMainScreenSettings(appPrefs.loadAppLang()!!)

            when (result) {
                is BaseResult.Error -> errorResult(result)
                is BaseResult.Success -> successResponseGetMainScreen(result.data)
            }
        }
    }

    /**
     * Обработка корректного ответа от сервера
     */
    private fun successResponseGetMainScreen(data: ResponseMainScreen) {

        if (data.result!!) {
            _mainScreenState.postValue(MainScreenState.Success(data))
            _listConn.postValue(connMapper.map(data.data?.srv))

            dispatchers.launchUI(viewModelScope){
                delay(1000)
                pingAllConnItem()
            }

        } else _mainScreenState.postValue(MainScreenState.Error("Произошла ошибка"))
    }

    /**
     * В данном методе мы должны пингануть все объекты из присланных
     */
    fun pingAllConnItem() {

        _listConn.value?.forEach { server ->
            pingServer(server)
        }
    }

    private fun pingServer(server: SrvItemUi?) {
        dispatchers.launchBackground(viewModelScope) {

            val service = ServiceConnectionItem(connUseCase.constractBaseUrl(server!!), okHttpClient, responseWrapper)
            val result = service.getApiData(server.ping!!)

            when (result) {

                // Если не удалось достучаться до пинг сервера значит ставим статус работы сервера false
                is BaseResult.Error -> {

                    if (result.err.code == 1) pingServer(server)

                    else if (result.err.code != 0) {
                        _listConn.newListIo {
                            if (it?.id == server.id) it?.copy(workStatus = false)
                            else it?.copy()
                        }

                    } else _mainScreenState.postValue(MainScreenState.NoInternet(result.err.message))
                }

                // Если не удалось достучаться до пинг сервера значит ставим статус работы сервера true
                is BaseResult.Success -> {

                    _listConn.newListIo {
                        if (it?.id == server.id) it?.copy(workStatus = true)
                        else it?.copy()
                    }
                }
            }
        }
    }

    /**
     * Вынос обработки ошибки
     */
    private fun errorResult(result: BaseResult.Error<Failure>) {
        if (result.err.code == 1) getMainScreenUi()
        else if (result.err.code != 0)
            _mainScreenState.postValue(MainScreenState.Error(result.err.message))
        else
            _mainScreenState.postValue(MainScreenState.NoInternet(result.err.message))
    }

    /**
     * Обработка нажатия на красную кнопку
     */
    fun clickRedBtn(baseUrl: String, action: String) {

        // TODO: сделать обработку нажатия, сделать апи

    }
}

sealed class MainScreenState {

    // Успешный запрос данных для страницы
    class Success(val data: ResponseMainScreen) : MainScreenState()

    // Состояние ошибки
    class Error(val messageError: String) : MainScreenState()

    // Ошибка когда отсутствует интернет
    class NoInternet(val messageError: String) : MainScreenState()

    // Загрузка при каких то долгих запросах
    object Loading : MainScreenState()
}
