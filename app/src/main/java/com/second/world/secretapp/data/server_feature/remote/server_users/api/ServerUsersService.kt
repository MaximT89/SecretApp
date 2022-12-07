package com.second.world.secretapp.data.server_feature.remote.server_users.api

import com.second.world.secretapp.data.server_feature.remote.server_users.model.response.ResponseServerUsers
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface ServerUsersService {

    @GET
    suspend fun getServerUsers(@Url url : String) : Response<ResponseServerUsers>
}