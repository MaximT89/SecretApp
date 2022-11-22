package com.second.world.secretapp.data.users_feature.remote.api

import com.second.world.secretapp.data.users_feature.remote.model.request.RequestUsersUpdate
import com.second.world.secretapp.data.users_feature.remote.model.response.ResponseUsersUpdate
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UsersUpdateService {

    @POST("/calc/update.user/")
    suspend fun updateUserFromServer(@Body request : RequestUsersUpdate) : Response<ResponseUsersUpdate>
}