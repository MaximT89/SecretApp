package com.second.world.secretapp.data.users_feature.repository

import com.second.world.secretapp.data.users_feature.remote.source.UsersCloudDataSource
import javax.inject.Inject

class RepositoryUsers @Inject constructor(
    private val cloudDataSource: UsersCloudDataSource
) {

    suspend fun getAllUsers() = cloudDataSource.getAllUsersFromServer()
}