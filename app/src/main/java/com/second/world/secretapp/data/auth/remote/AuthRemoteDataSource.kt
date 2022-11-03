package com.second.world.secretapp.data.auth.remote

import com.second.world.secretapp.core.bases.BaseResult
import com.second.world.secretapp.core.remote.Failure
import com.second.world.secretapp.core.remote.ResponseWrapper
import com.second.world.secretapp.data.auth.remote.model.ResponseAuth
import javax.inject.Inject

class AuthRemoteDataSource @Inject constructor(
    private val api: AuthService,
    private val responseWrapper: ResponseWrapper,
) {

    suspend fun getSms() : BaseResult<ResponseAuth, Failure> =
        responseWrapper.handleResponse {
            api.getSms()
        }

}