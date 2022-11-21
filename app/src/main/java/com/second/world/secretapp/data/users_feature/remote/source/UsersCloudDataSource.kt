package com.second.world.secretapp.data.users_feature.remote.source

import com.second.world.secretapp.core.bases.BaseResult
import com.second.world.secretapp.core.remote.Failure
import com.second.world.secretapp.core.remote.ResponseWrapper
import com.second.world.secretapp.data.app.local.AppPrefs
import com.second.world.secretapp.data.users_feature.remote.api.UsersAllService
import com.second.world.secretapp.data.users_feature.remote.model.request.RequestUsersAll
import com.second.world.secretapp.data.users_feature.remote.model.response.ResponseUsersAll
import javax.inject.Inject

class UsersCloudDataSource @Inject constructor(
    private val responseWrapper: ResponseWrapper,
    private val usersAllApi: UsersAllService,
    private val appPrefs: AppPrefs
) {

    suspend fun getAllUsersFromServer(): BaseResult<ResponseUsersAll, Failure> =
        responseWrapper.handleResponse {
            usersAllApi.getUsersAllFromServer(RequestUsersAll(appPrefs.loadAppLang()))
        }



}