package com.second.world.secretapp.data.users_feature.remote.api

import com.second.world.secretapp.data.users_feature.remote.model.request.RequestUsersDelete
import com.second.world.secretapp.data.users_feature.remote.model.response.ResponseUsersDelete
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UsersDeleteService {

    @POST("/calc/delete.user/")
    suspend fun deleteUserFromServer(@Body request: RequestUsersDelete): Response<ResponseUsersDelete>
}