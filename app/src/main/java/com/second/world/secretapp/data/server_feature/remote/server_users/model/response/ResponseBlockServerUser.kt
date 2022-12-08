package com.second.world.secretapp.data.server_feature.remote.server_users.model.response

import com.google.gson.annotations.SerializedName

data class ResponseBlockServerUser(

	@field:SerializedName("result")
	val result: Boolean? = null,

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("error")
	val error: List<Any?>? = null
)

data class Data(
	val any: Any? = null
)
