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
    private val _secretPin = MutableLiveData<Int>()
    private val _isAuth = MutableLiveData<Boolean>()
    private val _destination = MutableLiveData<Int>()
    val destination: LiveData<Int> = _destination

    // calculator
    private val _currentNumber = MutableLiveData("0")
    val currentNumber: LiveData<String> = _currentNumber

    private val _currentOperation = MutableLiveData<Operation>()
    val currentOperation: LiveData<Operation> = _currentOperation

    init {
        checkUserIsAuth()
        loadSecretPin()
    }

    // calculator methods
    fun addNewValue(value: String) {
        _currentNumber.value = if (_currentNumber.value == "0") value
        else {
            if (_currentNumber.value?.length!! <= 13) _currentNumber.value.plus(value)
            else _currentNumber.value
        }
    }

    fun convertToPercent() {
        if (_currentNumber.value != "0") _currentNumber.value =
            (_currentNumber.value?.toDouble()?.div(100)).toString()
    }

    fun backspace() {
        if (_currentNumber.value != "0") {
            val s = _currentNumber.value?.dropLast(1)
            if (s == "") _currentNumber.value = "0"
            else _currentNumber.value = s
        }
    }

    fun clearCurrentNumber() {
        _currentNumber.value = "0"
    }

    fun setOperation(operation: Operation) {
        _currentOperation.value = operation
    }

    fun addComma(){
        val s = _currentNumber.value
        if (!s!!.contains(".")) _currentNumber.value = _currentNumber.value.plus(".")
    }

    // core methods
    private fun checkUserIsAuth() {
        _isAuth.value = appPrefs.loadUserIsAuth()
    }

    /**
     * Берем секретный пин из префов и вставляем его в локальную переменную для дальней сверки
     */
    private fun loadSecretPin() {
        _secretPin.value = appPrefs.loadUserSecretPin()
    }

    fun checkSecretPin(value: Int) {
        if (value == _secretPin.value) {
            _destination.value =
                if (_isAuth.value == true) Destinations.CALCULATOR_TO_MAIN.id
                else Destinations.CALCULATOR_TO_AUTH.id
        }
    }
}

enum class Operation {
    PLUS,
    MINUS,
    TIMES,
    DIV,
    EQUAL
}