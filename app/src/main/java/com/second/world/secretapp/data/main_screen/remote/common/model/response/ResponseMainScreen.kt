package com.second.world.secretapp.data.main_screen.remote.common.model.response

import com.google.gson.annotations.SerializedName

data class ResponseMainScreen(

    @field:SerializedName("result")
    val result: Boolean? = null,

    @field:SerializedName("data")
    val data: Data? = null,

    @field:SerializedName("error")
    val error: List<String?>? = null,

    @field:SerializedName("version")
    val version: String? = null
)

data class StatusText(

    @field:SerializedName("off")
    val off: String? = null,

    @field:SerializedName("on")
    val on: String? = null
)

data class Conn(

    @field:SerializedName("protocol")
    val protocol: String? = null,

    @field:SerializedName("method")
    val method: String? = null,

    @field:SerializedName("port")
    val port: String? = null,

    @field:SerializedName("ping")
    val ping: String? = null,

    @field:SerializedName("ip")
    val ip: String? = null,

    @field:SerializedName("action")
    val action: String? = null,

    @field:SerializedName("action_test")
    val actionTest: String? = null,

    @field:SerializedName("status_text")
    val statusText: StatusText? = null
)

data class SrvItem(

    @field:SerializedName("conn")
    val conn: Conn? = null,

    @field:SerializedName("name")
    val name: String? = null,

//    @field:SerializedName("next_screen_conn")
//    val nextScreenConn: String? = null
)

data class Settings(

    @field:SerializedName("title_name")
    val titleName: String? = null
)

data class Data(

    @field:SerializedName("settings")
    val settings: Settings? = null,

    @field:SerializedName("srv")
    val srv: List<SrvItem?>? = null
)
