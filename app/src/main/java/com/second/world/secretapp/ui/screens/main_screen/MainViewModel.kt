package com.second.world.secretapp.ui.screens.main_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.second.world.secretapp.core.bases.BaseViewModel
import com.second.world.secretapp.data.app.local.AppPrefs
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val appPrefs: AppPrefs) : BaseViewModel() {

    private val _userIsAuth = MutableLiveData<Boolean>()
    val userIsAuth : LiveData<Boolean> = _userIsAuth

    init {
        getIsAuth()
    }

    private fun getIsAuth() {
        _userIsAuth.value = appPrefs.loadUserIsAuth()
    }

    private fun saveIsAuth(status : Boolean) {
        appPrefs.saveUserIsAuth(status)
    }

    fun logout() {
        saveIsAuth(false)
        getIsAuth()
    }
}
