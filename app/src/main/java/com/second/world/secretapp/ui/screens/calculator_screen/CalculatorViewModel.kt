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
    private val _currentNumber = MutableLiveData<String>()
    val currentNumber: LiveData<String> = _currentNumber

    init {
        checkUserIsAuth()
        loadSecretPin()
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