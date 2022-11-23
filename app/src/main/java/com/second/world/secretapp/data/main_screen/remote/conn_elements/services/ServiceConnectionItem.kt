package com.second.world.secretapp.data.main_screen.remote.conn_elements.services

import com.second.world.secretapp.core.bases.BaseResult
import com.second.world.secretapp.core.remote.Failure
import com.second.world.secretapp.core.remote.ResponseWrapper
import com.second.world.secretapp.data.main_screen.remote.conn_elements.api.ConnService
import com.second.world.secretapp.data.main_screen.remote.conn_elements.api.ResBtnService
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class ServiceConnectionItem @Inject constructor(
    baseUrl: String,
    okHttpClient : OkHttpClient,
    private val responseWrapper: ResponseWrapper,
    val id : Int
) {

    var retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .build()

    val connApi : ConnService = retrofit.create(ConnService::class.java)
    val redBtnApi : ResBtnService = retrofit.create(ResBtnService::class.java)

    suspend fun getApiData(url : String) : BaseResult<Int, Failure> {

        return responseWrapper.handleResponse {
            connApi.connPingApi(url)
        }
    }

    suspend fun redBtnClick(url : String) : BaseResult<ResponseBody, Failure> {

        return responseWrapper.handleResponse {
            redBtnApi.redBtnClickApi(url)
        }
    }
}