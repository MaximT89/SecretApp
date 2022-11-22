package com.second.world.secretapp.data.users_feature.remote.api

import com.second.world.secretapp.data.users_feature.remote.model.request.RequestUsersAdd
import com.second.world.secretapp.data.users_feature.remote.model.response.ResponseUsersAdd
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UsersAddService {

    @POST("/calc/add.user/")
    suspend fun addNewUserOnServer(@Body request : RequestUsersAdd) : Response<ResponseUsersAdd>
}