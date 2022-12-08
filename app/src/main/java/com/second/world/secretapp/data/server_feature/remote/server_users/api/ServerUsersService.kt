package com.second.world.secretapp.data.server_feature.remote.server_users.api

import com.second.world.secretapp.data.server_feature.remote.server_users.model.request.RequestSendMessage
import com.second.world.secretapp.data.server_feature.remote.server_users.model.response.ResponseBlockServerUser
import com.second.world.secretapp.data.server_feature.remote.server_users.model.response.ResponseSendMessage
import com.second.world.secretapp.data.server_feature.remote.server_users.model.response.ResponseServerUsers
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface ServerUsersService {

    @GET
    suspend fun getServerUsers(@Url url : String) : Response<ResponseServerUsers>

    @POST("24s2b3ztk6518olswltr0yphdmo22pv3dt3idjzr/")
    suspend fun sendMessageServerUser(@Body request : RequestSendMessage) : Response<ResponseSendMessage>

    @GET
    suspend fun blockServerUser(@Url url : String) : Response<ResponseBlockServerUser>

}