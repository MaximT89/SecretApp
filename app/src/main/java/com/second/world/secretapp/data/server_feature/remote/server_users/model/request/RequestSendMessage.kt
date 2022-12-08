package com.second.world.secretapp.data.server_feature.remote.server_users.model.request

import com.google.gson.annotations.SerializedName

data class RequestSendMessage(

    @field:SerializedName("user_id")
    val userId: Int? = null,

    @field:SerializedName("msg")
    val message: String? = null
)