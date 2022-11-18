package com.second.world.secretapp.data.main_screen.remote.conn_elements.source

import com.second.world.secretapp.core.bases.BaseResult
import com.second.world.secretapp.core.remote.Failure
import com.second.world.secretapp.core.remote.ResponseWrapper
import com.second.world.secretapp.data.main_screen.remote.conn_elements.api.ConnService
import javax.inject.Inject

class ConnCloudDataSource @Inject constructor(
    private val api: ConnService,
    private val responseWrapper: ResponseWrapper
) {

    suspend fun pingItemConn(url: String): BaseResult<Int, Failure> =
        responseWrapper.handleResponse {
            api.connPingApi(url)
        }

}