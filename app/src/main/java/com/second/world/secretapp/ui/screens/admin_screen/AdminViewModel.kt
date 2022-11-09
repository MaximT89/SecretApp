package com.second.world.secretapp.ui.screens.admin_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.second.world.secretapp.core.bases.BaseViewModel
import com.second.world.secretapp.data.app.local.AppPrefs
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val appPrefs: AppPrefs
): BaseViewModel() {

    private val _token = MutableLiveData<String>()
    val token : LiveData<String> = _token

    private val _secretPin = MutableLiveData<String>()
    val secretPin : LiveData<String> = _secretPin

    init {
        getToken()
        getSecretPin()
    }

    private fun getSecretPin() {
        _secretPin.value = appPrefs.loadUserSecretPin().toString()
    }

    private fun getToken() {
        _token.value = appPrefs.loadTokenApi()
    }
}