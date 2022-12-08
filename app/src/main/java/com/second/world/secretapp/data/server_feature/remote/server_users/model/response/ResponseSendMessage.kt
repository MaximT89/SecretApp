package com.second.world.secretapp.data.server_feature.remote.server_users.model.response

import com.google.gson.annotations.SerializedName

data class ResponseSendMessage(

	@field:SerializedName("result")
	val result: Boolean? = null,

	@field:SerializedName("data")
	val data: DataSendMessage? = null,

	@field:SerializedName("error")
	val error: List<Any?>? = null
)

data class DataSendMessage(
	val any: Any? = null
)
