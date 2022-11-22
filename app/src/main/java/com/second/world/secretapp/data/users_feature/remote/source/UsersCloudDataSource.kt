package com.second.world.secretapp.data.users_feature.remote.source

import com.second.world.secretapp.core.bases.BaseResult
import com.second.world.secretapp.core.remote.Failure
import com.second.world.secretapp.core.remote.ResponseWrapper
import com.second.world.secretapp.data.app.local.AppPrefs
import com.second.world.secretapp.data.users_feature.remote.api.UsersAddService
import com.second.world.secretapp.data.users_feature.remote.api.UsersAllService
import com.second.world.secretapp.data.users_feature.remote.api.UsersDeleteService
import com.second.world.secretapp.data.users_feature.remote.api.UsersUpdateService
import com.second.world.secretapp.data.users_feature.remote.model.request.RequestUsersAdd
import com.second.world.secretapp.data.users_feature.remote.model.request.RequestUsersAll
import com.second.world.secretapp.data.users_feature.remote.model.request.RequestUsersDelete
import com.second.world.secretapp.data.users_feature.remote.model.request.RequestUsersUpdate
import com.second.world.secretapp.data.users_feature.remote.model.response.ResponseUsersAdd
import com.second.world.secretapp.data.users_feature.remote.model.response.ResponseUsersAll
import com.second.world.secretapp.data.users_feature.remote.model.response.ResponseUsersDelete
import com.second.world.secretapp.data.users_feature.remote.model.response.ResponseUsersUpdate
import javax.inject.Inject

class UsersCloudDataSource @Inject constructor(
    private val responseWrapper: ResponseWrapper,
    private val usersAllApi: UsersAllService,
    private val usersAddApi: UsersAddService,
    private val usersUpdateApi: UsersUpdateService,
    private val usersDeleteApi: UsersDeleteService,
    private val appPrefs: AppPrefs
) {

    suspend fun getAllUsersFromServer(): BaseResult<ResponseUsersAll, Failure> =
        responseWrapper.handleResponse {
            usersAllApi.getUsersAllFromServer(RequestUsersAll(appPrefs.loadAppLang()))
        }

    suspend fun addUserToServer(request: RequestUsersAdd): BaseResult<ResponseUsersAdd, Failure> =
        responseWrapper.handleResponse {
            usersAddApi.addNewUserOnServer(request)
        }

    suspend fun updateUserFromServer(request: RequestUsersUpdate): BaseResult<ResponseUsersUpdate, Failure> =
        responseWrapper.handleResponse {
            usersUpdateApi.updateUserFromServer(request)
        }

    suspend fun deleteUserFromServer(phone: String): BaseResult<ResponseUsersDelete, Failure> =
        responseWrapper.handleResponse {
            usersDeleteApi.deleteUserFromServer(RequestUsersDelete(phone, appPrefs.loadAppLang()))
        }
}