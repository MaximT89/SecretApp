package com.second.world.secretapp.data.auth_screen.remote.model.responses

import com.google.gson.annotations.SerializedName

data class ResponseGetSms(

	@field:SerializedName("result")
	val result: Boolean? = null,

	@field:SerializedName("data")
	val data: Any? = null,

	@field:SerializedName("error")
	val error: List<String?>? = null
)
