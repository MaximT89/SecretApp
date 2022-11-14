package com.second.world.secretapp.data.auth_screen.remote.api

import com.second.world.secretapp.data.auth_screen.remote.model.requests.RequestGetSms
import com.second.world.secretapp.data.auth_screen.remote.model.requests.RequestGetUserData
import com.second.world.secretapp.data.auth_screen.remote.model.responses.ResponseGetSms
import com.second.world.secretapp.data.auth_screen.remote.model.responses.ResponseGetUserData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("calc/get.auth/")
    suspend fun getSmsRequest(@Body request : RequestGetSms) : Response<ResponseGetSms>

    @POST("calc/get.authcode/")
    suspend fun getUserDataRequest(@Body request : RequestGetUserData) : Response<ResponseGetUserData>
}