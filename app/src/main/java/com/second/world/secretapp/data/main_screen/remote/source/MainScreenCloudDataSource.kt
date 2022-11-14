package com.second.world.secretapp.data.main_screen.remote.source

import com.second.world.secretapp.core.bases.BaseResult
import com.second.world.secretapp.core.remote.Failure
import com.second.world.secretapp.core.remote.ResponseWrapper
import com.second.world.secretapp.data.main_screen.remote.api.MainScreenService
import com.second.world.secretapp.data.main_screen.remote.model.request.RequestMainScreen
import com.second.world.secretapp.data.main_screen.remote.model.response.ResponseMainScreen
import javax.inject.Inject

class MainScreenCloudDataSource @Inject constructor(
    private val responseWrapper: ResponseWrapper,
    private val api: MainScreenService
) {

    suspend fun getMainScreenSettings(lang: String): BaseResult<ResponseMainScreen, Failure> =
        responseWrapper.handleResponse {
            api.getMainScreenSettings(RequestMainScreen(lang = lang))
        }
}