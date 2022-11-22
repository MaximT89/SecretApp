package com.second.world.secretapp.ui.screens.users_add_screen

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.second.world.secretapp.core.bases.BaseResult
import com.second.world.secretapp.core.bases.BaseViewModel
import com.second.world.secretapp.core.bases.Dispatchers
import com.second.world.secretapp.core.remote.Failure
import com.second.world.secretapp.data.users_feature.remote.model.request.RequestUsersAdd
import com.second.world.secretapp.data.users_feature.remote.model.response.ResponseUsersAdd
import com.second.world.secretapp.data.users_feature.remote.model.response.ResponseUsersAll
import com.second.world.secretapp.data.users_feature.repository.RepositoryUsers
import com.second.world.secretapp.ui.screens.users_all_screen.UsersAllStates
import com.second.world.secretapp.ui.screens.users_all_screen.model.TextSettingModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UsersAddViewModel @Inject constructor(
    private val repository: RepositoryUsers,
    private val dispatchers: Dispatchers
) : BaseViewModel() {

    private val _usersAddState = MutableLiveData<UsersAddStates>()
    val usersAddState: LiveData<UsersAddStates> = _usersAddState

    fun addUser(name: String?, phone: String) {

        if(validData(name, phone)){
            dispatchers.launchBackground(viewModelScope) {
                val result = repository.addUserToServerUsers(RequestUsersAdd(phone, name))

                when (result) {
                    is BaseResult.Error -> errorAddUser(result) { addUser(name, phone) }
                    is BaseResult.Success -> successAddUser(result.data)
                }
            }
        } else _usersAddState.value = UsersAddStates.Error("Проверьте введенные данные")
    }

    private fun validData(name: String?, phone: String): Boolean {
        if (phone.length == 10 && !TextUtils.isEmpty(name)) return true
        return false
    }

    private fun successAddUser(data: ResponseUsersAdd) {
        if (data.result!!) _usersAddState.postValue(UsersAddStates.Success(data))
        else _usersAddState.postValue(UsersAddStates.Error("Произошла ошибка"))
    }

    private fun errorAddUser(result: BaseResult.Error<Failure>, addUserRepeat: () -> Unit) {
        if (result.err.code == 1) addUserRepeat.invoke()
        else if (result.err.code != 0) _usersAddState.postValue(UsersAddStates.Error(result.err.message))
        else _usersAddState.postValue(UsersAddStates.NoInternet(result.err.message))
    }
}

sealed class UsersAddStates {
    // Успешный запрос данных для страницы
    class Success(val data: ResponseUsersAdd) : UsersAddStates()

    // Состояние ошибки
    class Error(val messageError: String) : UsersAddStates()

    // Ошибка когда отсутствует интернет
    class NoInternet(val messageError: String) : UsersAddStates()

    // Загрузка при каких то долгих запросах
    object Loading : UsersAddStates()
}