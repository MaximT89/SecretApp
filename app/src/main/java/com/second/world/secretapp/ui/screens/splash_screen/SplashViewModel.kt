package com.second.world.secretapp.ui.screens.splash_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.second.world.secretapp.core.bases.BaseViewModel
import com.second.world.secretapp.data.app.local.AppPrefs
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val appPrefs: AppPrefs): BaseViewModel() {

    private var _isAuth = MutableLiveData<Boolean>()
    val isAuth : LiveData<Boolean> = _isAuth

    init {
        checkUserIsAuth()
    }

    private fun checkUserIsAuth() {
        _isAuth.value = appPrefs.loadUserIsAuth()
    }
}