package com.second.world.secretapp.data.server_feature.remote.conn_elements.model

import com.google.gson.annotations.SerializedName

data class ResponsePingServer(

	@field:SerializedName("result")
	val result: Boolean? = null,

	@field:SerializedName("data")
	val data: DataPingServer? = null,

	@field:SerializedName("error")
	val error: List<String?>? = null
)

data class DataPingServer(
	val any: Any? = null
)
