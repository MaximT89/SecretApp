package com.second.world.secretapp.data.users_feature.remote.model.response

import com.google.gson.annotations.SerializedName

data class ResponseUsersUpdate(

	@field:SerializedName("result")
	val result: Boolean? = null,

	@field:SerializedName("data")
	val data: DataUpdate? = null,

	@field:SerializedName("error")
	val error: List<String?>? = null,

	@field:SerializedName("version")
	val version: String? = null
)

data class DataUpdate(

	@field:SerializedName("user")
	val user: User? = null
)

data class User(

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("active")
	val active: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null
)
