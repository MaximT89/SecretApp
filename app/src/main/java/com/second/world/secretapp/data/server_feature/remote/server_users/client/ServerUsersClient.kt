package com.second.world.secretapp.data.server_feature.remote.server_users.client

import com.second.world.secretapp.core.bases.BaseResult
import com.second.world.secretapp.core.remote.Failure
import com.second.world.secretapp.core.remote.ResponseWrapper
import com.second.world.secretapp.data.server_feature.remote.conn_elements.model.ResponsePingServer
import com.second.world.secretapp.data.server_feature.remote.server_users.api.ServerUsersService
import com.second.world.secretapp.data.server_feature.remote.server_users.model.response.ResponseServerUsers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class ServerUsersClient @Inject constructor(
    baseUrl: String,
    okHttpClient : OkHttpClient,
    private val responseWrapper: ResponseWrapper,
) {

    var retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .build()

    val serverUsersApi : ServerUsersService = retrofit.create(ServerUsersService::class.java)

    suspend fun getServerUsers(url : String) : BaseResult<ResponseServerUsers, Failure> {
        return responseWrapper.handleResponse {
            serverUsersApi.getServerUsers(url)
        }
    }


}