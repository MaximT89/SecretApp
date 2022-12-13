package com.second.world.secretapp.data.server_feature.remote.conn_elements.client

import com.second.world.secretapp.core.bases.BaseResult
import com.second.world.secretapp.core.remote.Failure
import com.second.world.secretapp.core.remote.ResponseWrapper
import com.second.world.secretapp.data.server_feature.remote.conn_elements.api.ConnService
import com.second.world.secretapp.data.server_feature.remote.conn_elements.api.ResBtnService
import com.second.world.secretapp.data.server_feature.remote.conn_elements.model.ResponsePingServer
import com.second.world.secretapp.ui.screens.main_screen.model_ui.SrvItemUi
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

data class ConnectionItemClient @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val responseWrapper: ResponseWrapper,
    val connUi: SrvItemUi?,
    val id: Int? = connUi?.id,
    var serverWorkStatus : Boolean? = null
) {

    var retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(getBaseUrl())
        .client(okHttpClient)
        .build()

    val connApi: ConnService = retrofit.create(ConnService::class.java)
    val redBtnApi: ResBtnService = retrofit.create(ResBtnService::class.java)

    fun getBaseUrl() = "${connUi?.protocol}://${connUi?.ip}:${connUi?.port}/"

    suspend fun getPingResult(): BaseResult<ResponsePingServer, Failure> {

        return responseWrapper.handleResponse {
            connApi.connPingApi(connUi?.ping!!)
        }
    }

    suspend fun redBtnClick(): BaseResult<ResponseBody, Failure> {

        return responseWrapper.handleResponse {
            redBtnApi.redBtnClickApi(connUi?.action!!)
        }
    }
}