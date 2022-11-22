package com.second.world.secretapp.data.users_feature.repository

import com.second.world.secretapp.data.users_feature.remote.model.request.RequestUsersAdd
import com.second.world.secretapp.data.users_feature.remote.model.request.RequestUsersUpdate
import com.second.world.secretapp.data.users_feature.remote.source.UsersCloudDataSource
import javax.inject.Inject

class RepositoryUsers @Inject constructor(
    private val cloudDataSource: UsersCloudDataSource
) {

    suspend fun getAllUsers() = cloudDataSource.getAllUsersFromServer()

    suspend fun addUserToServerUsers(request: RequestUsersAdd) =
        cloudDataSource.addUserToServer(request)

    suspend fun updateUserFromServer(request: RequestUsersUpdate) =
        cloudDataSource.updateUserFromServer(request)

    suspend fun deleteUserFromServer(phone: String) =
        cloudDataSource.deleteUserFromServer(phone)

}