package com.second.world.secretapp.data.server_feature.remote.server_users.model.response

import com.google.gson.annotations.SerializedName

data class ResponseBlockServerUser(

	@field:SerializedName("result")
	val result: Boolean? = null,

	@field:SerializedName("data")
	val data: DataBlockServerUser? = null,

	@field:SerializedName("error")
	val error: List<String?>? = null
)

data class DataBlockServerUser(
	val any: Any? = null
)
