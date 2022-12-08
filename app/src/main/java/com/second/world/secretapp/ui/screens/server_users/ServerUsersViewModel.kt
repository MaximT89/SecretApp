package com.second.world.secretapp.ui.screens.server_users

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.second.world.secretapp.core.bases.BaseResult
import com.second.world.secretapp.core.bases.BaseViewModel
import com.second.world.secretapp.core.bases.Dispatchers
import com.second.world.secretapp.core.remote.Failure
import com.second.world.secretapp.core.remote.ResponseWrapper
import com.second.world.secretapp.data.server_feature.remote.common.model.response.ResponseMainScreen
import com.second.world.secretapp.data.server_feature.remote.server_users.client.ServerUsersClient
import com.second.world.secretapp.data.server_feature.remote.server_users.model.response.ResponseServerUsers
import com.second.world.secretapp.data.server_feature.remote.server_users.model.response.ServerUsersItem
import com.second.world.secretapp.domain.server_users_screen.ServerUsersInteractor
import com.second.world.secretapp.ui.screens.main_screen.MainScreenState
import com.second.world.secretapp.ui.screens.main_screen.model_ui.NextScreenConnUI
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import javax.inject.Inject

@HiltViewModel
class ServerUsersViewModel @Inject constructor(
    private val dispatchers: Dispatchers,
    private val serverUsersInteractor: ServerUsersInteractor,
    private val okHttpClient: OkHttpClient,
    private val responseWrapper: ResponseWrapper,
) : BaseViewModel() {

    private val _serverUsersState = MutableLiveData<ServerUsersState>(ServerUsersState.EmptyState)
    val serverUsersState: LiveData<ServerUsersState> = _serverUsersState

    private val _apiClient = MutableLiveData<ServerUsersClient>()

    private val _listServerUsers = MutableLiveData<List<ServerUsersItem?>?>()

    fun getSaveClientAndGetUsers(connItem: NextScreenConnUI) {
        createApiClient(connItem)

        getUsers(connItem.action)
    }

    private fun getUsers(action: String?) {
        dispatchers.launchBackground(viewModelScope) {
            // TODO: запросить данные с сервера через новый клиент

            if(action != null){

                val result: BaseResult<ResponseServerUsers, Failure>? = _apiClient.value?.getServerUsers(action)

                when(result) {
                    is BaseResult.Error -> {
                        if (result.err.code == 1) getUsers(action)
                        else if (result.err.code != 0)  _serverUsersState.postValue(ServerUsersState.Error(result.err.message))
                        else _serverUsersState.postValue(ServerUsersState.NoInternet(result.err.message))
                    }

                    is BaseResult.Success -> {
                        if(result.data.result == true){
                            _serverUsersState.postValue(ServerUsersState.Success(result.data))
                            _listServerUsers.postValue(result.data.data?.users)
                        }else {
                            _serverUsersState.postValue(ServerUsersState.Error("Ошибка: ответ от сервера успешен, но result = false"))
                        }
                    }
                    null -> _serverUsersState.postValue(ServerUsersState.Error("Ошибка:Нулевой запрос"))
                }
            }
        }
    }

    private fun createApiClient(connItem: NextScreenConnUI) {

        val apiClient = ServerUsersClient(
            okHttpClient = okHttpClient,
            baseUrl = serverUsersInteractor.constractBaseUrlServerUsers(connItem),
            responseWrapper = responseWrapper
        )

        _apiClient.value = apiClient
    }
}

sealed class ServerUsersState {

    // Стартовое состояние
    object EmptyState : ServerUsersState()

    // Успешный запрос данных для страницы
    class Success(val data: ResponseServerUsers) : ServerUsersState()

    // Состояние ошибки
    class Error(val messageError: String) : ServerUsersState()

    // Ошибка когда отсутствует интернет
    class NoInternet(val messageError: String) : ServerUsersState()

    // Загрузка при каких то долгих запросах
    object Loading : ServerUsersState()

    // State для тестов
    class Test(val testText: String) : ServerUsersState()
}