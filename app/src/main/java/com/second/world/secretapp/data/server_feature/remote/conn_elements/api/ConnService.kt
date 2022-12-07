package com.second.world.secretapp.data.server_feature.remote.conn_elements.api

import com.second.world.secretapp.data.server_feature.remote.conn_elements.model.ResponsePingServer
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface ConnService {

    @GET
    suspend fun connPingApi(@Url url : String) : Response<ResponsePingServer>
}