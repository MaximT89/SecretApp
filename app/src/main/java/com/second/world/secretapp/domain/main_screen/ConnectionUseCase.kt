package com.second.world.secretapp.domain.main_screen

import com.second.world.secretapp.core.extension.log
import com.second.world.secretapp.ui.screens.main_screen.model_ui.SrvItemUi
import javax.inject.Inject

class ConnectionUseCase @Inject constructor() {

    fun constractBaseUrl(data : SrvItemUi?) : String {

        log("CONNECTION BASE URL: ${data?.protocol}://${data?.ip}:${data?.port}/")

        return "${data?.protocol}://${data?.ip}:${data?.port}/"
    }
}