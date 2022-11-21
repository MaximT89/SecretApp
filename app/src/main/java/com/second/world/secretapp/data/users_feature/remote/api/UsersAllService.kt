package com.second.world.secretapp.data.users_feature.remote.api

import com.second.world.secretapp.data.users_feature.remote.model.request.RequestUsersAll
import com.second.world.secretapp.data.users_feature.remote.model.response.ResponseUsersAll
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UsersAllService {

    @POST("/calc/get.users/")
    suspend fun getUsersAllFromServer(@Body request : RequestUsersAll) : Response<ResponseUsersAll>
}