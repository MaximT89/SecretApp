package com.second.world.secretapp.data.auth_screen.remote.source

import com.second.world.secretapp.core.bases.BaseResult
import com.second.world.secretapp.core.remote.Failure
import com.second.world.secretapp.core.remote.ResponseWrapper
import com.second.world.secretapp.data.auth_screen.remote.api.AuthService
import com.second.world.secretapp.data.auth_screen.remote.model.requests.RequestGetSms
import com.second.world.secretapp.data.auth_screen.remote.model.requests.RequestGetUserData
import com.second.world.secretapp.data.auth_screen.remote.model.responses.ResponseGetSms
import com.second.world.secretapp.data.auth_screen.remote.model.responses.ResponseGetUserData
import javax.inject.Inject

class AuthCloudDataSource @Inject constructor(
    private val api: AuthService,
    private val responseWrapper: ResponseWrapper,
) {

    suspend fun getSms(request: RequestGetSms) : BaseResult<ResponseGetSms, Failure> =
        responseWrapper.handleResponse {
            api.getSmsRequest(request)
        }


    suspend fun getUserData(request: RequestGetUserData) : BaseResult<ResponseGetUserData, Failure> =
        responseWrapper.handleResponse {
            api.getUserDataRequest(request)
        }
}