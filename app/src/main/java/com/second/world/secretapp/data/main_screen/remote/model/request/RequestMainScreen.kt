package com.second.world.secretapp.data.main_screen.remote.model.request

import com.google.gson.annotations.SerializedName

data class RequestMainScreen(

    @field:SerializedName("lang")
    val lang: String
)