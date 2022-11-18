package com.second.world.secretapp.ui.screens

import com.second.world.secretapp.core.bases.BaseViewModel
import com.second.world.secretapp.data.app.local.AppPrefs
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(private val appPrefs: AppPrefs) : BaseViewModel() {

    private fun saveIsAuth(status: Boolean) {
        appPrefs.saveUserIsAuth(status)
    }

    fun logout() {
        saveIsAuth(false)
    }
}