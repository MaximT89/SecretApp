package com.second.world.secretapp.ui.screens.users_detail_screen

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.second.world.secretapp.core.bases.BaseResult
import com.second.world.secretapp.core.bases.BaseViewModel
import com.second.world.secretapp.core.bases.Dispatchers
import com.second.world.secretapp.core.remote.Failure
import com.second.world.secretapp.data.users_feature.remote.model.request.RequestUsersUpdate
import com.second.world.secretapp.data.users_feature.remote.model.response.ResponseUsersDelete
import com.second.world.secretapp.data.users_feature.remote.model.response.ResponseUsersUpdate
import com.second.world.secretapp.data.users_feature.remote.model.response.UsersItem
import com.second.world.secretapp.data.users_feature.repository.RepositoryUsers
import com.second.world.secretapp.ui.screens.users_all_screen.model.TextSettingModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UsersUpdateViewModel @Inject constructor(
    private val repository: RepositoryUsers,
    private val dispatchers: Dispatchers
) : BaseViewModel() {

    private val _usersUpdateState = MutableLiveData<UsersUpdateStates>()
    val usersUpdateState: LiveData<UsersUpdateStates> = _usersUpdateState

    private val _userData = MutableLiveData<UsersItem?>()
    val userData: LiveData<UsersItem?> = _userData

    private val _pageTextSettings = MutableLiveData<TextSettingModel?>()
    val pageTextSettings: LiveData<TextSettingModel?> = _pageTextSettings

    fun updateUserFromServer(name: String, phone: String, active: Boolean) {

        if (validData(name, phone)) {
            _usersUpdateState.value = UsersUpdateStates.Loading
            dispatchers.launchBackground(viewModelScope) {

                val result =
                    repository.updateUserFromServer(RequestUsersUpdate(phone, name, active))

                when (result) {
                    is BaseResult.Error -> errorUpdateUser(result) {
                        updateUserFromServer(
                            name = name,
                            phone = phone,
                            active = active
                        )
                    }
                    is BaseResult.Success -> successUpdateUser(result.data)
                }
            }
        } else _usersUpdateState.value = UsersUpdateStates.PhoneNotCorrect("Проверьте введенные данные")
    }

    private fun successUpdateUser(data: ResponseUsersUpdate) {
        if (data.result!!) _usersUpdateState.postValue(UsersUpdateStates.Success(data))
        else _usersUpdateState.postValue(UsersUpdateStates.Error("Произошла ошибка"))
    }

    private fun errorUpdateUser(result: BaseResult.Error<Failure>, updateUserRepeat: () -> Unit) {
        if (result.err.code == 1) updateUserRepeat.invoke()
        else if (result.err.code != 0) _usersUpdateState.postValue(UsersUpdateStates.Error(result.err.message))
        else _usersUpdateState.postValue(UsersUpdateStates.NoInternet(result.err.message))
    }

    fun deleteUser() {

        val phone = _userData.value?.phone

        if (validPhone(phone)) {

            _usersUpdateState.value = UsersUpdateStates.Loading
            dispatchers.launchBackground(viewModelScope) {

                val result = repository.deleteUserFromServer(phone!!)

                when (result) {
                    is BaseResult.Error -> errorDeleteUser(result) { deleteUser() }
                    is BaseResult.Success -> successDeleteUser(result.data)
                }
            }
        } else _usersUpdateState.value = UsersUpdateStates.Error("Проверьте введенные данные")
    }

    private fun successDeleteUser(data: ResponseUsersDelete) {
        if (data.result!!) _usersUpdateState.postValue(UsersUpdateStates.SuccessDeleteUser(data))
        else _usersUpdateState.postValue(UsersUpdateStates.Error("Произошла ошибка"))
    }

    // TODO: отрефакторить перевести на один метод error
    private fun errorDeleteUser(result: BaseResult.Error<Failure>, deleteUserRepeat: () -> Unit) {
        if (result.err.code == 1) deleteUserRepeat.invoke()
        else if (result.err.code != 0) _usersUpdateState.postValue(UsersUpdateStates.Error(result.err.message))
        else _usersUpdateState.postValue(UsersUpdateStates.NoInternet(result.err.message))
    }

    private fun validData(name: String?, phone: String): Boolean {
        if (phone.length == 10 && !TextUtils.isEmpty(name)) return true
        return false
    }

    fun contentEnabled(status: Boolean) {
        if (status) _usersUpdateState.value = UsersUpdateStates.EditOn
        else _usersUpdateState.value = UsersUpdateStates.EditOff
    }

    fun updateUserData(data: UsersItem?) {
        _userData.value = data
    }

    private fun validPhone(phone: String?): Boolean {
        return !TextUtils.isEmpty(phone) && phone?.length == 10
    }

    fun savePageSettings(pageTexts: TextSettingModel) {
        _pageTextSettings.value = pageTexts
    }
}

sealed class UsersUpdateStates {

    // Успешный запрос данных для страницы
    class Success(val data: ResponseUsersUpdate) : UsersUpdateStates()

    // Успешное удаление юзера
    class SuccessDeleteUser(val data: ResponseUsersDelete) : UsersUpdateStates()

    // Неверно введен телефон
    class PhoneNotCorrect(val messageError: String) : UsersUpdateStates()

    // Состояние ошибки
    class Error(val messageError: String) : UsersUpdateStates()

    // Ошибка когда отсутствует интернет
    class NoInternet(val messageError: String) : UsersUpdateStates()

    // Загрузка при каких то долгих запросах
    object Loading : UsersUpdateStates()

    // Редактирование разрешено
    object EditOn : UsersUpdateStates()

    // Редактирование запрещено
    object EditOff : UsersUpdateStates()
}