package com.second.world.secretapp.data.auth.remote.model.requests

import com.google.gson.annotations.SerializedName

data class RequestGetSms(

    @field:SerializedName("phone")
    val phone: String,

    @field:SerializedName("lang")
    val lang: String?
)