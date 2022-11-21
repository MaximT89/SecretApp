package com.second.world.secretapp.data.users_feature.remote.model.request

import com.google.gson.annotations.SerializedName

data class RequestUsersAll(
    @field:SerializedName("lang")
    val lang: String? = null
)