package com.second.world.secretapp.domain.main_screen

import com.second.world.secretapp.data.main_screen.remote.common.model.response.Conn
import javax.inject.Inject

class ServiceConstractApi @Inject constructor() {

    fun constractBaseUrl(data : Conn) : String {
        return "${data.protocol}://${data.ip}/${data.port}/"
    }
}