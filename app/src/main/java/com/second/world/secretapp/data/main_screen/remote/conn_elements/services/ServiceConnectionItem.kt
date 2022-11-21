package com.second.world.secretapp.data.main_screen.remote.conn_elements.services

import com.second.world.secretapp.core.bases.BaseResult
import com.second.world.secretapp.core.extension.log
import com.second.world.secretapp.core.remote.Failure
import com.second.world.secretapp.core.remote.ResponseWrapper
import com.second.world.secretapp.data.main_screen.remote.conn_elements.api.ConnService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class ServiceConnectionItem @Inject constructor(
    baseUrl: String,
    okHttpClient : OkHttpClient,
    private val responseWrapper: ResponseWrapper
) {

    var retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .build()

    val api : ConnService = retrofit.create(ConnService::class.java)

    suspend fun getApiData(url : String) : BaseResult<Int, Failure> {

        return responseWrapper.handleResponse {
            api.connPingApi(url)
        }
    }
}