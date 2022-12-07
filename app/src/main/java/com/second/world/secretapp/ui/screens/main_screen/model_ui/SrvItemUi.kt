package com.second.world.secretapp.ui.screens.main_screen.model_ui

import com.second.world.secretapp.data.server_feature.common.Indicators

data class SrvItemUi(
    val protocol: String? = null,
    val method: String? = null,
    val port: String? = null,
    val ping: String? = null,
    val ip: String? = null,
    val action: String? = null,
    val actionTest: String? = null,
    val textStatusOn : String? = null,
    val textStatusOff : String? = null,
    val name: String? = null,

    val id: Int? = null,
    var indicator: Indicators? = null,

    var workStatus: Boolean? = null,
    var loading: Boolean? = null,
    var nextScreenConn: NextScreenConnUI? = null
)

data class NextScreenConnUI(
    val protocol: String? = null,
    val method: String? = null,
    val port: String? = null,
    val ping: Any? = null,
    val ip: String? = null,
    val action: String? = null,
    val actionTest: Any? = null
)