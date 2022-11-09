package com.second.world.secretapp.ui.screens.auth_screen

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.second.world.secretapp.core.bases.BaseResult
import com.second.world.secretapp.core.bases.BaseViewModel
import com.second.world.secretapp.core.bases.Dispatchers
import com.second.world.secretapp.core.extension.log
import com.second.world.secretapp.data.app.local.AppPrefs
import com.second.world.secretapp.data.auth.remote.model.ResponseAuth
import com.second.world.secretapp.data.auth.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val dispatchers: Dispatchers,
    private val appPrefs: AppPrefs
) : BaseViewModel() {

    private var _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    private var _smsCode = MutableLiveData<Int>()

    init {
        checkUserAuth()
    }

    private fun checkUserAuth() {
        if(appPrefs.loadUserIsAuth()){
            if (appPrefs.loadTokenApi() == "555") {
                _authState.value = AuthState.ChangeSecretPin
            } else {
                _authState.value = AuthState.SuccessAuth
            }
        }
    }

    // ТЕСТОВАЯ ЗАГЛУШКА
    // TODO: убрать после получения реального АПИ
    fun getSms(phone: String) {
        if (phoneIsCorrect(phone)) {
            _authState.value = AuthState.Loading
            viewModelScope.launch {
                delay(3000)
                _smsCode.value = 1111
                _authState.value = AuthState.SuccessGetSms(ResponseAuth(111))
            }
        } else _authState.value = AuthState.Error("Некорректный номер телефона")

    }

    /**
     * Метод запроса смс-кода с сервера
     */
//    fun getSms(phone: String) {
//
//        if (phoneIsCorrect(phone)) {
//            _authState.value = AuthState.Loading
//            dispatchers.launchBackground(viewModelScope) {
//                when (val result = repository.getSms(phone)) {
//                    is BaseResult.Error -> {
//                        if (result.err.code == 1) getSms(phone)
//                        else if (result.err.code != 0)
//                            _authState.postValue(AuthState.Error(result.err.message))
//                        else
//                            _authState.postValue(AuthState.NoInternet(result.err.message))
//                    }
//                    is BaseResult.Success -> {
//                        _smsCode.postValue(result.data.smsCode)
//                        _authState.postValue(
//                            AuthState.SuccessGetSms(
//                                result.data
//                            )
//                        )
//                    }
//                }
//            }
//        } else _authState.value = AuthState.Error("Некорректный номер телефона")
//
//    }

    /**
     * Валидация телефонного номера, так как в поле можно вводить только цифры, нам остается проверить
     * количество символов которые ввел юзер
     */
    private fun phoneIsCorrect(phone: String): Boolean {
        return phone.length == 10
    }

    /**
     * Проверяем смс-код который вводит пользователь, он должен сходиться с тем что пришел с сервера
     */
    fun checkSmsCode(userCode: String) {
        if (userCode == _smsCode.value.toString()) {

            userIsAuth(true)

            if (checkUserSecretPin()) _authState.value = AuthState.SuccessAuth
            else _authState.value = AuthState.ChangeSecretPin

        } else _authState.value = AuthState.Error("Неверный код")
    }

    private fun userIsAuth(newStatus : Boolean) {
        appPrefs.saveUserIsAuth(newStatus)
    }

    /**
     * Проверяем секретный пароль, если он равен 555, значит нужно этот пароль изменить
     */
    private fun checkUserSecretPin(): Boolean {
        val secretPin = appPrefs.loadUserSecretPin()
        if (secretPin == 555) return false
        return true
    }

    /**
     * Валидация нового пин-кода
     */
    fun updateNewPin(firstPin: String, secondPin: String) {
        if (TextUtils.isEmpty(firstPin) || TextUtils.isEmpty(secondPin)){
            _authState.value = AuthState.Error("Введите новый pin-код")
        } else if (firstPin != secondPin) {
            _authState.value = AuthState.Error("Pin-коды не совпадают")
        } else if (firstPin.length < 3 || firstPin.length > 8) {
            _authState.value = AuthState.Error("Pin-код не соответствует длине (от 3 до 8 символов)")
        } else if (firstPin.toCharArray().toSet().size == 1) {
            _authState.value = AuthState.Error("Pin-код не может состоять из одной цифры")
        } else {
            appPrefs.saveUserSecretPin(firstPin.toInt())
            _authState.value = AuthState.SuccessAuth
        }
    }
}

/**
 * Возможные состояния экрана авторизации
 */
sealed class AuthState {

    // Успешное получение смс без ошибки
    class SuccessGetSms(val data: ResponseAuth) : AuthState()

    // Полное успешная авторизация (секретный код уже был изменен ранее)
    object SuccessAuth : AuthState()

    // Состояние ошибки
    class Error(val messageError: String) : AuthState()

    // Ошибка когда отсутствует интернет
    class NoInternet(val messageError: String) : AuthState()

    // Загрузка при каких то долгих запросах
    object Loading : AuthState()

    // Изменение секретного пина юзера
    object ChangeSecretPin : AuthState()

}