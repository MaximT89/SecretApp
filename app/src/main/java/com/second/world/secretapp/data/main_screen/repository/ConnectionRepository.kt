package com.second.world.secretapp.data.main_screen.repository

import com.second.world.secretapp.core.bases.BaseResult
import com.second.world.secretapp.core.remote.Failure
import com.second.world.secretapp.data.main_screen.remote.conn_elements.source.ConnCloudDataSource
import javax.inject.Inject

class ConnectionRepository @Inject constructor(private val cloudDataSource: ConnCloudDataSource) {

    suspend fun pingItemConn(url: String): BaseResult<Int, Failure> {
        return cloudDataSource.pingItemConn(url)
    }


}