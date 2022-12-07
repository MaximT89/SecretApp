package com.second.world.secretapp.data.server_feature.remote.common.source

import com.second.world.secretapp.core.bases.BaseResult
import com.second.world.secretapp.core.remote.Failure
import com.second.world.secretapp.core.remote.ResponseWrapper
import com.second.world.secretapp.data.server_feature.remote.common.api.MainScreenService
import com.second.world.secretapp.data.server_feature.remote.common.model.request.RequestMainScreen
import com.second.world.secretapp.data.server_feature.remote.common.model.response.ResponseMainScreen
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