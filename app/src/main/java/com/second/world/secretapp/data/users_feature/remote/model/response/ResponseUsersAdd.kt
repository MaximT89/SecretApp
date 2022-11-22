package com.second.world.secretapp.data.users_feature.remote.model.response

import com.google.gson.annotations.SerializedName

data class ResponseUsersAdd(

	@field:SerializedName("result")
	val result: Boolean? = null,

	@field:SerializedName("data")
	val data: DataUsersAdd? = null,

	@field:SerializedName("error")
	val error: List<String?>? = null,

	@field:SerializedName("version")
	val version: String? = null
)

data class DataUsersAdd(

	@field:SerializedName("id")
	val id: Int? = null
)
