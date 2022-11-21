package com.second.world.secretapp.ui.screens.auth_screen

import android.os.CountDownTimer
import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.second.world.secretapp.core.bases.BaseResult
import com.second.world.secretapp.core.bases.BaseViewModel
import com.second.world.secretapp.core.bases.Dispatchers
import com.second.world.secretapp.core.remote.Failure
import com.second.world.secretapp.data.auth_screen.remote.model.requests.RequestGetSms
import com.second.world.secretapp.data.auth_screen.remote.model.requests.RequestGetUserData
import com.second.world.secretapp.data.auth_screen.remote.model.responses.ResponseGetSms
import com.second.world.secretapp.data.auth_screen.remote.model.responses.ResponseGetUserData
import com.second.world.secretapp.data.auth_screen.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val dispatchers: Dispatchers,
) : BaseViewModel() {

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    private val _userPhone = MutableLiveData<String>()

    private val _timerSecond = MutableLiveData("")
    val timerSecond: LiveData<String> = _timerSecond

    init {
        checkUserAuth()
    }

    /**
     * Создание таймера для возможности повторно получить смс (таймер выносим в main поток)
     */
    fun timerStart() {
        dispatchers.launchUI(viewModelScope){
            _authState.value = AuthState.Timer(true)
            object : CountDownTimer(10000, 1000) {
                override fun onTick(p: Long) {
                    _timerSecond.value = (p / 1000).toString()
                }

                override fun onFinish() {
                    _authState.value = AuthState.Timer(false)
                }
            }.start()
        }
    }

    /**
     * Проверяем авторизацию юзера и введенный секретный код, для дальнейшей навигации
     */
    private fun checkUserAuth() {
        if (repository.loadUserIsAuth()) {
            if (repository.loadUserSecretPin() == 555) _authState.value = AuthState.ChangeSecretPin
            else _authState.value = AuthState.SuccessAuth
        }
    }

    /**
     * Сохраняем временно телефон
     */
    fun saveUserPhone(phone: String) {
        _userPhone.value = phone
    }

    /**
     * Метод запроса смс-кода с сервера
     */
    fun getSms(phone: String) {

        if (phoneIsCorrect(phone)) {

            _authState.value = AuthState.Loading

            dispatchers.launchBackground(viewModelScope) {
                when (val result = repository.getSms(RequestGetSms(phone, repository.loadLang()))) {
                    is BaseResult.Error -> errorResult(result) { getSms(phone) }
                    is BaseResult.Success -> successResponseGetSms(result.data)
                }
            }
        } else _authState.value = AuthState.Error("Некорректный номер телефона")
    }

    /**
     * Метод для отправки смс кода на сервер и получение ответа
     */
    fun getUserData(code: String) {

        _authState.value = AuthState.Loading
        dispatchers.launchBackground(viewModelScope) {

            when (val result = repository.getUserData(
                RequestGetUserData(
                    phone = _userPhone.value!!,
                    code = code,
                    lang = repository.loadLang()
                )
            )) {
                is BaseResult.Error -> errorResult(result) { getUserData(code) }
                is BaseResult.Success -> successResponseGetUserData(result.data)
            }
        }
    }

    /**
     * Выносим код обработки ошибки при запросе на сервер
     */
    private fun errorResult(
        result: BaseResult.Error<Failure>,
        protocolError: () -> Unit
    ) {
        if (result.err.code == 1) protocolError.invoke()
        else if (result.err.code != 0)
            _authState.postValue(AuthState.Error(result.err.message))
        else
            _authState.postValue(AuthState.NoInternet(result.err.message))
    }

    /**
     * Обработка успешного ответа при отправки смс кода
     */
    private fun successResponseGetUserData(data: ResponseGetUserData) {
        if (data.result!!) {

            userIsAuth(true)
            repository.saveToken(data.data?.token!!)

            validateSecretCode()

        } else _authState.postValue(AuthState.Error("Произошла ошибка"))
    }

    /**
     * Если пришел успешный ответ, то нам еще нужно проверить пришел ли result
     * false или нет, если да, то просто выводить ошибку
     */
    private fun successResponseGetSms(data: ResponseGetSms) {
        if (data.result!!) {
            _authState.postValue(AuthState.SuccessGetSms(data))
            timerStart()
        }
        else _authState.postValue(AuthState.Error("Произошла ошибка"))
    }

    /**
     * Валидация телефонного номера, так как в поле можно вводить только цифры, нам остается проверить
     * количество символов которые ввел юзер
     */
    private fun phoneIsCorrect(phone: String): Boolean {
        return phone.length == 10
    }

    /**
     * Валидация секретного кода пользователя
     */
    fun validateSecretCode() {
        if (checkUserSecretPin()) _authState.postValue(AuthState.SuccessAuth)
        else _authState.postValue(AuthState.ChangeSecretPin)
    }

    private fun userIsAuth(newStatus: Boolean) {
        repository.saveUserIsAuth(newStatus)
    }

    /**
     * Проверяем секретный пароль, если он равен 555, значит нужно этот пароль изменить
     */
    private fun checkUserSecretPin(): Boolean {
        val secretPin = repository.loadUserSecretPin()
        if (secretPin == 555) return false
        return true
    }

    fun changeAuthStateToStart() {
        _authState.value = AuthState.StartState
    }

    /**
     * Валидация нового пин-кода
     */
    fun updateNewPin(firstPin: String, secondPin: String) {
        if (TextUtils.isEmpty(firstPin) || TextUtils.isEmpty(secondPin)) {
            _authState.value = AuthState.Error("Введите новый pin-код")
        } else if (firstPin != secondPin) {
            _authState.value = AuthState.Error("Pin-коды не совпадают")
        } else if (firstPin.length < 3 || firstPin.length > 8) {
            _authState.value =
                AuthState.Error("Pin-код не соответствует длине (от 3 до 8 символов)")
        } else if (firstPin.toCharArray().toSet().size == 1) {
            _authState.value = AuthState.Error("Pin-код не может состоять из одной цифры")
        } else {
            repository.saveUserSecretPin(firstPin.toInt())
            _authState.value = AuthState.SuccessAuth
        }
    }
}

/**
 * Возможные состояния экрана авторизации
 */
sealed class AuthState {

    // Стартовое состояние экрана с вводом номера телефона
    object StartState : AuthState()

    // Состояние для обображения таймера
    class Timer(val showTimer: Boolean = false) : AuthState()

    // Успешное получение смс без ошибки
    class SuccessGetSms(val data: ResponseGetSms) : AuthState()

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