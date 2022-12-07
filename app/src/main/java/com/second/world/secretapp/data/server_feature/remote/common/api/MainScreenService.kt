package com.second.world.secretapp.data.server_feature.remote.common.api

import com.second.world.secretapp.data.server_feature.remote.common.model.request.RequestMainScreen
import com.second.world.secretapp.data.server_feature.remote.common.model.response.ResponseMainScreen
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface MainScreenService {

    @POST("calc/get.main/")
    suspend fun getMainScreenSettings(@Body request : RequestMainScreen) : Response<ResponseMainScreen>
}