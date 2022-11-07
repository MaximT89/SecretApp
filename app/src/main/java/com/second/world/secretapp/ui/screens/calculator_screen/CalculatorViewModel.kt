package com.second.world.secretapp.ui.screens.calculator_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.second.world.secretapp.core.bases.BaseViewModel
import com.second.world.secretapp.core.navigation.Destinations
import com.second.world.secretapp.data.app.local.AppPrefs
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CalculatorViewModel @Inject constructor(private val appPrefs: AppPrefs) : BaseViewModel() {

    // core
    private val _secretPinFromPref = MutableLiveData<Int>()
    private val _isAuth = MutableLiveData<Boolean>()
    private val _destination = MutableLiveData<Int>()
    val destination: LiveData<Int> = _destination

    private val _allUserInput = MutableLiveData<String>()
    val allUserInput: LiveData<String> = _allUserInput

    // calculator
    private val _numberForUser = MutableLiveData("0")
    val numberForUser: LiveData<String> = _numberForUser

    private val _firstNumber = MutableLiveData("")
    val firstNumber: LiveData<String> = _firstNumber

    private val _secondNumber = MutableLiveData("")
    val secondNumber: LiveData<String> = _secondNumber

    private val _currentOperation = MutableLiveData(Operation.EMPTY)
    private val _operationFirst = MutableLiveData(false)

    private val _finalDataText = MutableLiveData("")
    val finalDataText: LiveData<String> = _finalDataText


    init {
        checkUserIsAuth()
        loadSecretPin()
    }

    // calculator methods
    fun addNewValue(value: String) {
        updateUserInput(value)

        _numberForUser.value = if (_numberForUser.value == "0") value
        else {
            if (_numberForUser.value?.length!! <= 13) {
                _numberForUser.value.plus(value)
            } else {
                _numberForUser.value
            }
        }
    }

    private fun updateUserInput(value: String) {
        _allUserInput.value = _allUserInput.value.plus(value)
    }

    fun convertToPercent() {
        if (_numberForUser.value != "0") _numberForUser.value =
            (_numberForUser.value?.toDouble()?.div(100)).toString()
    }

    fun backspace() {
        if (_numberForUser.value != "0") {
            val s = _numberForUser.value?.dropLast(1)
            if (s == "") _numberForUser.value = "0"
            else _numberForUser.value = s
        }
    }

    fun clearCurrentNumber() {
        _numberForUser.value = "0"
        updateFinalData()
    }

    fun setOperation(operation: Operation) {
        if (_operationFirst.value == false) {
            _currentOperation.value = operation
            _firstNumber.value = _numberForUser.value
            _numberForUser.value = "0"
            updateFinalData()
        } else {
            _secondNumber.value = _numberForUser.value
        }

    }

    fun addComma() {
        val s = _numberForUser.value
        if (!s!!.contains(".")) _numberForUser.value = _numberForUser.value.plus(".")
    }

    fun updateFinalData() {
        _finalDataText.value = _firstNumber.value
            ?.plus(_currentOperation.value?.equalString)
            ?.plus(_secondNumber.value)
    }

    // core methods
    private fun checkUserIsAuth() {
        _isAuth.value = appPrefs.loadUserIsAuth()
    }

    /**
     * Берем секретный пин из префов и вставляем его в локальную переменную для дальней сверки
     */
    private fun loadSecretPin() {
        _secretPinFromPref.value = appPrefs.loadUserSecretPin()
    }

    fun checkSecretPin(value: String) {
        if (value.contains(_secretPinFromPref.value.toString())) {
            _allUserInput.value = ""
            _destination.value =
                if (_isAuth.value == true) Destinations.CALCULATOR_TO_MAIN.id
                else Destinations.CALCULATOR_TO_AUTH.id
        }
    }
}

enum class Operation(val equalString: String) {
    EMPTY(""),
    PLUS("+"),
    MINUS("-"),
    TIMES("*"),
    DIV("/"),
    EQUAL("=")
}