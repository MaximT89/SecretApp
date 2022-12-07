package com.second.world.secretapp.ui.screens.main_screen.model_ui

import com.second.world.secretapp.core.bases.Mapper
import com.second.world.secretapp.data.server_feature.remote.common.model.response.SrvItem
import javax.inject.Inject

class MapperConnDataToUI @Inject constructor() : Mapper<List<SrvItem?>?, List<SrvItemUi?>?> {
    override fun map(data: List<SrvItem?>?): List<SrvItemUi?>? {

        fun getRandomId() = (1..999999).random()

        return data?.map {
            SrvItemUi(
                protocol = it?.conn?.protocol,
                method = it?.conn?.method,
                port = it?.conn?.port,
                ping = it?.conn?.ping,
                ip = it?.conn?.ip,
                action = it?.conn?.action,
                actionTest = it?.conn?.actionTest,
                textStatusOn = it?.conn?.statusText?.on,
                textStatusOff = it?.conn?.statusText?.off,
                name = it?.name,
                id = getRandomId(),
                nextScreenConn = it?.nextScreenConn?.let { nextScreenConn ->
                    NextScreenConnUI(
                        protocol = nextScreenConn.protocol,
                        method = nextScreenConn.method,
                        port = nextScreenConn.port,
                        ping = nextScreenConn.ping,
                        ip = nextScreenConn.ip,
                        action = nextScreenConn.action,
                        actionTest = nextScreenConn.actionTest,
                    )
                }
            )
        }
    }
}