package com.second.world.secretapp.data.server_feature.remote.server_users.client

import com.second.world.secretapp.core.bases.BaseResult
import com.second.world.secretapp.core.remote.Failure
import com.second.world.secretapp.core.remote.ResponseWrapper
import com.second.world.secretapp.data.server_feature.remote.conn_elements.model.ResponsePingServer
import com.second.world.secretapp.data.server_feature.remote.server_users.api.ServerUsersService
import com.second.world.secretapp.data.server_feature.remote.server_users.model.request.RequestSendMessage
import com.second.world.secretapp.data.server_feature.remote.server_users.model.response.ResponseBlockServerUser
import com.second.world.secretapp.data.server_feature.remote.server_users.model.response.ResponseSendMessage
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
    companion object {
        const val SEND_MSG = "24s2b3ztk6518olswltr0yphdmo22pv3dt3idjzr/"
        const val BLOCK_USER = "zzlcpepqy42o91oemyv0xzt7ztpqmjp36zxqyu5t/"
    }


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

    suspend fun sendMessageServerUser(request :RequestSendMessage) : BaseResult<ResponseSendMessage, Failure> {
        return responseWrapper.handleResponse {
            serverUsersApi.sendMessageServerUser(request)
        }
    }

    // example "zzlcpepqy42o91oemyv0xzt7ztpqmjp36zxqyu5t/testuser/670/"
    suspend fun blockServerUser(userName : String, userId : Int) : BaseResult<ResponseBlockServerUser, Failure> {
        return responseWrapper.handleResponse {
            serverUsersApi.blockServerUser("$BLOCK_USER/$userName/$userId/")
        }
    }
}