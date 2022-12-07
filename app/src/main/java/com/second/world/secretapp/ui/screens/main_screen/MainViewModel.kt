package com.second.world.secretapp.ui.screens.main_screen

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.second.world.secretapp.core.bases.BaseResult
import com.second.world.secretapp.core.bases.BaseViewModel
import com.second.world.secretapp.core.bases.Dispatchers
import com.second.world.secretapp.core.extension.newListIo
import com.second.world.secretapp.core.remote.Failure
import com.second.world.secretapp.core.remote.ResponseWrapper
import com.second.world.secretapp.data.server_feature.remote.common.model.response.ResponseMainScreen
import com.second.world.secretapp.data.server_feature.remote.conn_elements.model.ResponsePingServer
import com.second.world.secretapp.data.server_feature.remote.conn_elements.services.ServiceConnectionItem
import com.second.world.secretapp.data.server_feature.repository.MainScreenRepository
import com.second.world.secretapp.domain.app_interactor.AppInteractor
import com.second.world.secretapp.domain.main_screen.ConnectionUseCase
import com.second.world.secretapp.ui.screens.main_screen.model_ui.MapperConnDataToUI
import com.second.world.secretapp.ui.screens.main_screen.model_ui.SrvItemUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dispatchers: Dispatchers,
    private val repositoryMain: MainScreenRepository,
    private val connMapper: MapperConnDataToUI,
    private val connUseCase: ConnectionUseCase,
    private val responseWrapper: ResponseWrapper,
    private val okHttpClient: OkHttpClient,
    private val appInteractor: AppInteractor
) : BaseViewModel() {

    private val _mainScreenState = MutableLiveData<MainScreenState>()
    val mainScreenState: LiveData<MainScreenState> = _mainScreenState

    private val _listConn = MutableLiveData<List<SrvItemUi?>?>()
    val listConn: LiveData<List<SrvItemUi?>?> = _listConn

    init {
        getMainScreenUi()
    }

    /**
     * Стартовый запрос серверов с сервера
     */
    private fun getMainScreenUi() {

        _mainScreenState.value = MainScreenState.Loading

        dispatchers.launchBackground(viewModelScope) {

            val result: BaseResult<ResponseMainScreen, Failure> =
                repositoryMain.getMainScreenSettings()

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

            dispatchers.launchUI(viewModelScope) {
                delay(1000)
                pingAllConnItem()
            }

            startPingServers()

            // TODO: Сверка версии приложения
            // Сверка версии приложения, пока что не нужно
            // checkVersion(data.version)

        } else _mainScreenState.postValue(MainScreenState.Error("Произошла ошибка"))
    }

    /**
     * Сверка версии приложения и версии доступной
     */
    private fun checkVersion(version: String?) {
        if (version != null) {
            _mainScreenState.postValue(
                MainScreenState.VersionValidateState(!appInteractor.validateVersion(version))
            )
        }
    }

    /**
     * В данном методе мы должны пингануть все объекты из присланных
     */
    fun pingAllConnItem() {

        _listConn.value?.forEach { server ->
            pingServer(server)
        }
    }

    /**
     * Пингуем отдельно выбранный сервер
     */
    private fun pingServer(server: SrvItemUi?) {
        dispatchers.launchBackground(viewModelScope) {

            val service = ServiceConnectionItem(
                connUseCase.constractBaseUrl(server!!),
                okHttpClient,
                responseWrapper,
                server.id
            )

            val result: BaseResult<ResponsePingServer, Failure> =
                service.getPingResult(server.ping!!)

            when (result) {

                // Если не удалось достучаться до пинг сервера значит ставим статус работы сервера false
                is BaseResult.Error -> {

                    if (result.err.code == 1) pingServer(server)
                    else if (result.err.code != 0) updateStatusServer(
                        serverId = server.id,
                        newStatus = false
                    )
                    else _mainScreenState.postValue(MainScreenState.NoInternet(result.err.message))
                }

                // Если удалось достучаться до пинг сервера значит ставим статус работы сервера true
                is BaseResult.Success -> {
                    if (result.data.result == true) updateStatusServer(
                        serverId = server.id,
                        newStatus = true
                    )
                    else updateStatusServer(serverId = server.id, newStatus = false)
                }
            }
        }
    }

    private fun updateStatusServer(serverId: Int?, newStatus: Boolean) {
        _listConn.newListIo {
            if (it?.id == serverId) it?.copy(workStatus = newStatus)
            else it?.copy()
        }
    }

    fun startPingServers() {
        dispatchers.launchUI(viewModelScope) {
            object : CountDownTimer(5000, 1000) {
                override fun onTick(p0: Long) {}

                override fun onFinish() {
                    pingAllConnItem()
                    startPingServers()
                }
            }.start()
        }
    }

    /**
     * Вынос обработки ошибки
     */
    private fun errorResult(result: BaseResult.Error<Failure>) {
        if (result.err.code == 1) getMainScreenUi()
        else if (result.err.code != 0) _mainScreenState.postValue(MainScreenState.Error(result.err.message))
        else _mainScreenState.postValue(MainScreenState.NoInternet(result.err.message))
    }

    /**
     * Обработка нажатия на красную кнопку
     */
    fun clickRedBtn(serverData: SrvItemUi) {

        dispatchers.launchBackground(viewModelScope) {

            val service = ServiceConnectionItem(
                connUseCase.constractBaseUrl(serverData),
                okHttpClient,
                responseWrapper,
                serverData.id
            )

            val result = service.redBtnClick(serverData.action!!)

            when (result) {
                is BaseResult.Error -> {
                    if (result.err.code == 1) clickRedBtn(serverData)
                    else if (result.err.code != 0)
                        _mainScreenState.postValue(MainScreenState.ErrorRedBtn("ошибка отключения"))
                    else
                        _mainScreenState.postValue(MainScreenState.NoInternet(result.err.message))
                }
                is BaseResult.Success -> MainScreenState.SuccessRedBtn(result.data)
            }
        }
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

    // State для проверки новой версии приложения
    class VersionValidateState(val showNotification: Boolean) : MainScreenState()

    class SuccessRedBtn(val data: ResponseBody) : MainScreenState()

    class ErrorRedBtn(val messageError: String) : MainScreenState()

    // State для тестов
    class Test(val testText: String) : MainScreenState()
}
