package com.second.world.secretapp.ui.main_activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.second.world.secretapp.core.bases.BaseViewModel
import com.second.world.secretapp.data.app.local.AppPrefs
import com.second.world.secretapp.ui.screens.users_all_screen.model.TextSettingModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(private val appPrefs: AppPrefs) : BaseViewModel() {

    private val _modelTextSetting = MutableLiveData<TextSettingModel>()

    private fun saveIsAuth(status: Boolean) {
        appPrefs.saveUserIsAuth(status)
    }

    fun logout() {
        saveIsAuth(false)
    }

    fun getTextSettings() : TextSettingModel? {
        return _modelTextSetting.value
    }

    fun saveTextSetting(data: TextSettingModel) {
        _modelTextSetting.value = data
    }
}