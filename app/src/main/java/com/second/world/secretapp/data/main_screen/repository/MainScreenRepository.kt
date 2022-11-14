package com.second.world.secretapp.data.main_screen.repository

import com.second.world.secretapp.core.bases.BaseResult
import com.second.world.secretapp.core.remote.Failure
import com.second.world.secretapp.data.app.local.AppPrefs
import com.second.world.secretapp.data.main_screen.remote.model.response.ResponseMainScreen
import com.second.world.secretapp.data.main_screen.remote.source.MainScreenCloudDataSource
import javax.inject.Inject

class MainScreenRepository @Inject constructor(
    private val appPrefs: AppPrefs,
    private val cloudDataSource: MainScreenCloudDataSource
) {

    suspend fun getMainScreenSettings(lang: String): BaseResult<ResponseMainScreen, Failure> =
        cloudDataSource.getMainScreenSettings(lang)
}