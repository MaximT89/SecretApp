package com.second.world.secretapp.data.auth.remote.model.responses

import com.google.gson.annotations.SerializedName

data class ResponseGetUserData(

	@field:SerializedName("result")
	val result: Boolean? = null,

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("error")
	val error: List<String?>? = null
)

data class Data(

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("last_login")
	val lastLogin: Double? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("active")
	val active: Boolean? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("token")
	val token: String? = null
)
