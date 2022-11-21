package com.second.world.secretapp.data.users_feature.remote.model.response

import com.google.gson.annotations.SerializedName

data class ResponseUsersAll(

	@field:SerializedName("result")
	val result: Boolean? = null,

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("error")
	val error: List<String?>? = null
)

data class UsersItem(

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("last_login")
	val lastLogin: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("active")
	val active: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null
)

data class Text(

	@field:SerializedName("title_page")
	val titlePage: String? = null
)

data class Data(

	@field:SerializedName("text")
	val text: Text? = null,

	@field:SerializedName("users")
	val users: List<UsersItem?>? = null
)
