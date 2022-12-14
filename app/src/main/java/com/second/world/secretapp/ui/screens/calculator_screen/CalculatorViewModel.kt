package com.second.world.secretapp.ui.screens.calculator_screen

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.second.world.secretapp.core.bases.BaseViewModel
import com.second.world.secretapp.core.constants.Constants
import com.second.world.secretapp.core.extension.log
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

    private val _allUserInput = MutableLiveData("")
    val allUserInput: LiveData<String> = _allUserInput

    // calculator
    private val _currentNumber = MutableLiveData("0")
    val currentNumber: LiveData<String> = _currentNumber

    private val _resultCalculate = MutableLiveData("")

    private val _operation = MutableLiveData(Operation.EMPTY)

    private val _finalText = MutableLiveData(Pair("", true))
    val finalText: LiveData<Pair<String, Boolean>> = _finalText

    // timer
    private var timer : CountDownTimer? = null

    init {
        checkUserIsAuth()
        loadSecretPin()
    }

    // calculator methods
    fun addNewValue(value: String) {
        updateUserInput(value)

        log("_currentNumber.value before: ${_currentNumber.value!!}")

        _currentNumber.value = if (_currentNumber.value == "0") value
        else {
            if (_currentNumber.value?.length!! <= 13) _currentNumber.value.plus(value)
            else _currentNumber.value
        }

        log("_currentNumber.value after: ${_currentNumber.value!!}")
        updateFinalText(true)
    }

    fun setOperation(operation: Operation) {

        if (_resultCalculate.value == "") {

            _resultCalculate.value = _currentNumber.value
            _currentNumber.value = "0"

            _operation.value = operation

            updateFinalText(true)
        } else {

//            if (_currentNumber.value != "0") {
//            }

            // TODO: ???????? ?????????????????? ???????? ???? currentNumber ???????? ?????? ?????? ???? ???? ?????????????????????? ????????????????, ?? ???????????? ?????????????? ????????????????

            when (_operation.value!!) {
                Operation.EMPTY -> {

                    log("Operation.EMPTY work")

                    if (_resultCalculate.value == "0") _resultCalculate.value = _currentNumber.value
                    else _resultCalculate.value = _resultCalculate.value

                    _currentNumber.value = "0"

                    _operation.value = operation
                    updateFinalText(true)
                }
                Operation.PLUS -> {

                    val newResult =
                        _resultCalculate.value?.toDouble()!!.plus(_currentNumber.value!!.toDouble())

                    if (newResult % 1 == 0.0) _resultCalculate.value =
                        newResult.toString().substringBefore(".")
                    else _resultCalculate.value = newResult.toString()

                    _currentNumber.value = "0"

                    _operation.value = operation
                    updateFinalText(true)
                    if (_operation.value == Operation.EQUAL) logicEqual()
                }
                Operation.MINUS -> {

                    val newResult =
                        _resultCalculate.value?.toDouble()!!
                            .minus(_currentNumber.value!!.toDouble())

                    if (newResult % 1 == 0.0) _resultCalculate.value =
                        newResult.toString().substringBefore(".")
                    else _resultCalculate.value = newResult.toString()

                    _currentNumber.value = "0"

                    _operation.value = operation
                    updateFinalText(true)
                    if (_operation.value == Operation.EQUAL) logicEqual()
                }
                Operation.TIMES -> {
                    val newResult =
                        _resultCalculate.value?.toDouble()!!
                            .times(_currentNumber.value!!.toDouble())

                    if (newResult % 1 == 0.0) _resultCalculate.value =
                        newResult.toString().substringBefore(".")
                    else _resultCalculate.value = newResult.toString()

                    _currentNumber.value = "0"

                    _operation.value = operation
                    updateFinalText(true)
                    if (_operation.value == Operation.EQUAL) logicEqual()

                }
                Operation.DIV -> {
                    val newResult =
                        _resultCalculate.value?.toDouble()!!.div(_currentNumber.value!!.toDouble())

                    if (newResult % 1 == 0.0) _resultCalculate.value =
                        newResult.toString().substringBefore(".")
                    else _resultCalculate.value = newResult.toString()

                    _currentNumber.value = "0"

                    _operation.value = operation
                    updateFinalText(true)
                    if (_operation.value == Operation.EQUAL) logicEqual()

                }
                Operation.EQUAL -> {}
            }
        }
    }

    private fun logicEqual() {

        log("logicEqual work")

        _currentNumber.value = "0"
        _resultCalculate.value = ""
        _operation.value = Operation.EMPTY
        _allUserInput.value = ""
    }

    private fun updateFinalText(status : Boolean) {

        log("updateFinalText work")

        _finalText.value = Pair(_resultCalculate.value.plus(_operation.value?.equalString).plus(
            if (_currentNumber.value == "0") ""
            else _currentNumber.value
        ), status)

    }

    private fun updateUserInput(value: String) {
        _allUserInput.value = _allUserInput.value.plus(value)
    }

    fun convertToPercent() {
        if (_currentNumber.value != "0") _currentNumber.value =
            (_currentNumber.value?.toDouble()?.div(100)).toString()

        updateFinalText(true)
    }

    fun backspace() {

        _allUserInput.value = _allUserInput.value?.dropLast(1)

        if (_currentNumber.value != "0") {
            val s = _currentNumber.value?.dropLast(1)
            if (s == "") _currentNumber.value = "0"
            else _currentNumber.value = s
        }
        updateFinalText(true)
    }

    fun clearCurrentNumber() {
        _currentNumber.value = "0"
        _resultCalculate.value = ""
        _operation.value = Operation.EMPTY
        _allUserInput.value = ""
        updateFinalText(true)
    }

    fun addComma() {
        val s = _currentNumber.value
        if (!s!!.contains(".")) {
            _currentNumber.value = _currentNumber.value.plus(".")
            updateFinalText(true)
        }
    }

    // core methods
    private fun checkUserIsAuth() {
        _isAuth.value = appPrefs.loadUserIsAuth()
    }

    /**
     * ?????????? ?????????????????? ?????? ???? ???????????? ?? ?????????????????? ?????? ?? ?????????????????? ???????????????????? ?????? ?????????????? ????????????
     */
    private fun loadSecretPin() {
        _secretPinFromPref.value = appPrefs.loadUserSecretPin()
    }

    fun checkSecretPin(value: String) {

        if (value == _secretPinFromPref.value.toString()) {

            startTimer {
                _allUserInput.value = ""
                _destination.value =
                    if (_isAuth.value == true) {
                        if (appPrefs.loadUserSecretPin() == 555) Destinations.CALCULATOR_TO_AUTH.id
                        else Destinations.CALCULATOR_TO_MAIN.id
                    } else Destinations.CALCULATOR_TO_AUTH.id
            }
        } else cancelTimer()
    }

    fun startTimer(callFinishTimer : () -> Unit) {

        timer = object : CountDownTimer(3000, 1000){
            override fun onTick(p0: Long) {}

            override fun onFinish() {
                callFinishTimer.invoke()
            }
        }.start()
    }

    fun cancelTimer(){
        timer?.cancel()
        timer = null
    }
}

enum class Operation(val equalString: String) {
    EMPTY(""),
    PLUS("+"),
    MINUS("-"),
    TIMES("*"),
    DIV("/"),
    EQUAL("")
}