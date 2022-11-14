package com.second.world.secretapp.data.auth_screen.remote.model.requests

import com.google.gson.annotations.SerializedName

data class RequestGetUserData(

    @field:SerializedName("phone")
    val phone: String,

    @field:SerializedName("code")
    val code: String,

    @field:SerializedName("lang")
    val lang: String
)