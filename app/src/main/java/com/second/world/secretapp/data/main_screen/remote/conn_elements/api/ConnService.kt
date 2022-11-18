package com.second.world.secretapp.data.main_screen.remote.conn_elements.api

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface ConnService {

    @GET
    suspend fun connPingApi(@Url url : String) : Response<Int>


    @GET
    suspend fun redButtonApi(@Url url : String) : Response<ResponseBody>
}