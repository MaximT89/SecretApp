package com.second.world.secretapp.ui.screens.users_all_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.second.world.secretapp.core.bases.BaseResult
import com.second.world.secretapp.core.bases.BaseViewModel
import com.second.world.secretapp.core.bases.Dispatchers
import com.second.world.secretapp.core.remote.Failure
import com.second.world.secretapp.data.users_feature.remote.model.response.ResponseUsersAll
import com.second.world.secretapp.data.users_feature.repository.RepositoryUsers
import com.second.world.secretapp.ui.screens.users_all_screen.model.TextSettingModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UsersAllViewModel @Inject constructor(
    private val repository: RepositoryUsers,
    private val dispatchers: Dispatchers
) : BaseViewModel() {

    private val _usersAllState = MutableLiveData<UsersAllStates>()
    val usersAllState: LiveData<UsersAllStates> = _usersAllState

    private val _modelTextSetting = MutableLiveData<TextSettingModel>()
    val modelTextSetting : LiveData<TextSettingModel> = _modelTextSetting

    fun getAllUsers() {
        _usersAllState.postValue(UsersAllStates.Loading)
        dispatchers.launchBackground(viewModelScope) {
            when (val result = repository.getAllUsers()) {
                is BaseResult.Error -> errorGetAllUsers(result)
                is BaseResult.Success -> successGetAllUsers(result.data)
            }
        }
    }

    fun getTextSettings() = _modelTextSetting.value

    private fun errorGetAllUsers(result: BaseResult.Error<Failure>) {
        if (result.err.code == 1) getAllUsers()
        else if (result.err.code != 0) _usersAllState.postValue(UsersAllStates.Error(result.err.message))
        else _usersAllState.postValue(UsersAllStates.NoInternet(result.err.message))
    }

    private fun successGetAllUsers(data: ResponseUsersAll) {
        if (data.result!!) {
            _usersAllState.postValue(UsersAllStates.Success(data))

            _modelTextSetting.postValue(TextSettingModel(
                addUserBtnText = data.data?.text?.addUserBtnText,
                nameUserBtnText = data.data?.text?.nameUserBtnText,
                phoneUserBtnText = data.data?.text?.phoneUserBtnText,
                saveUserBtnText = data.data?.text?.saveUserBtnText,
                titleAddUsersPage = data.data?.text?.titleAddUsersPage,
                titleAllUsersPage = data.data?.text?.titleAllUsersPage,
                titleUpdateUsersPage = data.data?.text?.titleUpdateUsersPage
            ))
        }
        else _usersAllState.postValue(UsersAllStates.Error("Произошла ошибка"))
    }
}

sealed class UsersAllStates {
    // Успешный запрос данных для страницы
    class Success(val data: ResponseUsersAll) : UsersAllStates()

    // Состояние ошибки
    class Error(val messageError: String) : UsersAllStates()

    // Ошибка когда отсутствует интернет
    class NoInternet(val messageError: String) : UsersAllStates()

    // Загрузка при каких то долгих запросах
    object Loading : UsersAllStates()
}
