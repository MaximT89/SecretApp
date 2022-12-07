package com.second.world.secretapp.data.server_feature.remote.common.model.request

import com.google.gson.annotations.SerializedName

data class RequestMainScreen(

    @field:SerializedName("lang")
    val lang: String
)