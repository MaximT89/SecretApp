package com.second.world.secretapp.data.main_screen.repository

import com.second.world.secretapp.core.bases.BaseResult
import com.second.world.secretapp.core.remote.Failure
import com.second.world.secretapp.data.app.local.AppPrefs
import com.second.world.secretapp.data.main_screen.remote.common.model.response.ResponseMainScreen
import com.second.world.secretapp.data.main_screen.remote.common.source.MainScreenCloudDataSource
import javax.inject.Inject

class MainScreenRepository @Inject constructor(
    private val cloudDataSource: MainScreenCloudDataSource,
    private val appPref : AppPrefs
) {

    suspend fun getMainScreenSettings(): BaseResult<ResponseMainScreen, Failure> =
        cloudDataSource.getMainScreenSettings(appPref.loadAppLang()!!)
}