package com.second.world.secretapp.ui.screens.main_screen

import android.os.CountDownTimer
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
import com.second.world.secretapp.data.server_feature.remote.common.model.response.ResponseMainScreen
import com.second.world.secretapp.data.server_feature.remote.conn_elements.model.ResponsePingServer
import com.second.world.secretapp.data.server_feature.remote.conn_elements.client.ConnectionItemClient
import com.second.world.secretapp.data.server_feature.repository.MainScreenRepository
import com.second.world.secretapp.domain.app_interactor.AppInteractor
import com.second.world.secretapp.domain.main_screen.ConnectionUseCase
import com.second.world.secretapp.ui.screens.main_screen.model_ui.MapperConnDataToUI
import com.second.world.secretapp.ui.screens.main_screen.model_ui.SrvItemUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dispatchers: Dispatchers,
    private val repositoryMain: MainScreenRepository,
    private val connMapper: MapperConnDataToUI,
    private val responseWrapper: ResponseWrapper,
    private val okHttpClient: OkHttpClient,
    private val appInteractor: AppInteractor,
) : BaseViewModel() {

    private val _mainScreenState = MutableLiveData<MainScreenState>()
    val mainScreenState: LiveData<MainScreenState> = _mainScreenState

    private val _listServerClients = MutableLiveData<List<ConnectionItemClient?>?>(listOf())
    val listServerClients : LiveData<List<ConnectionItemClient?>?> = _listServerClients

    private val timer = MutableLiveData<CountDownTimer?>()
    private val isRunningTimer = MutableLiveData(false)


    init {
        getMainScreenUi()
    }

    /**
     * Стартовый запрос серверов с сервера
     */
    private fun getMainScreenUi() {

        dispatchers.launchBackground(viewModelScope) {

            withContext(kotlinx.coroutines.Dispatchers.Main) {
                _mainScreenState.value = MainScreenState.Loading
            }

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
    private suspend fun successResponseGetMainScreen(data: ResponseMainScreen) {

        if (data.result!!) {

            // формируем дизайн главной страницы
            _mainScreenState.postValue(MainScreenState.Success(data))

            // собираем все конекшены
            createListConnections(data)

            dispatchers.launchUI(viewModelScope) {
                delay(1000)
                pingAllConnItem()
            }

            startPingServers()

        } else _mainScreenState.postValue(MainScreenState.Error("Произошла ошибка"))
    }

    private suspend fun createListConnections(data: ResponseMainScreen) {

//        _listConn.postValue(connMapper.map(data.data?.srv))

        val mappingData: List<SrvItemUi?>? = connMapper.map(data.data?.srv)

        val listServices = mutableListOf<ConnectionItemClient>()

        mappingData?.forEach { connUi ->
            val service = ConnectionItemClient(
                okHttpClient,
                responseWrapper,
                connUi
            )
            listServices.add(service)
        }

        _listServerClients.postValue(listServices)
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
        _listServerClients.value?.forEach { client ->
            pingServer(client)
        }
    }

    private fun pingServer(client: ConnectionItemClient?) {
        dispatchers.launchBackground(viewModelScope) {

            val result: BaseResult<ResponsePingServer, Failure>? = client?.getPingResult()

            when (result) {

                // Если не удалось достучаться до пинг сервера значит ставим статус работы сервера false
                is BaseResult.Error -> {
                    if (result.err.code == 1) pingServer(client)
                    else if (result.err.code != 0) updateStatusServer(
                        serverId = client.id,
                        newStatus = false
                    )
                    else _mainScreenState.postValue(MainScreenState.NoInternet(result.err.message))
                }

                // Если удалось достучаться до пинг сервера значит ставим статус работы сервера true
                is BaseResult.Success -> {
                    if (result.data.result == true) updateStatusServer(
                        serverId = client.id,
                        newStatus = true
                    )
                    else updateStatusServer(serverId = client.id, newStatus = false)
                }

                null -> updateStatusServer(serverId = client?.id, newStatus = false)
            }
        }
    }

    private fun updateStatusServer(serverId: Int?, newStatus: Boolean) {
        _listServerClients.newListIo {
            if (it?.id == serverId) it?.copy(serverWorkStatus = newStatus)
            else it?.copy()
        }
    }

    fun startPingServers() {
        dispatchers.launchUI(viewModelScope) {

            if (isRunningTimer.value == false) {
                timer.value = object : CountDownTimer(5000, 1000) {
                    override fun onTick(p0: Long) {
                        isRunningTimer.value = true
                    }

                    override fun onFinish() {
                        isRunningTimer.value = false
                        pingAllConnItem()
                        startPingServers()
                    }
                }.start()
            }
        }
    }

    fun stopTimer() {
        timer.value?.let {
            it.cancel()
            isRunningTimer.value = false
            timer.value = null
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
    fun clickRedBtn(idServer: Int) {

        dispatchers.launchBackground(viewModelScope) {

            _listServerClients.value?.forEach { client ->
                if (client?.id == idServer) {

                    val result = client.redBtnClick()

                    when (result) {
                        is BaseResult.Error -> {
                            if (result.err.code == 1) clickRedBtn(idServer)
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
    }

    fun disableAllServers() {
        dispatchers.launchBackground(viewModelScope){
            _listServerClients.value?.forEach { client ->
                client?.redBtnClick()
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
