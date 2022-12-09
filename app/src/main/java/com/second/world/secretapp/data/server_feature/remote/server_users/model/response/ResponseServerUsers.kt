package com.second.world.secretapp.data.server_feature.remote.server_users.model.response

import com.google.gson.annotations.SerializedName

data class ResponseServerUsers(

	@field:SerializedName("result")
	val result: Boolean? = null,

	@field:SerializedName("data")
	val data: ServerUsersData? = null,

	@field:SerializedName("error")
	val error: List<String?>? = null
)

data class ServerUsersItem(

	@field:SerializedName("time_sleep")
	val timeSleep: String? = null,

	@field:SerializedName("user_name")
	val userName: String? = null,

	@field:SerializedName("sess")
	val sess: String? = null,

	@field:SerializedName("time_in")
	val timeIn: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class ServerUsersConn(

	@field:SerializedName("proc_list")
	val procList: String? = null
)

data class ServerUsersData(

	@field:SerializedName("conn")
	val conn: ServerUsersConn? = null,

	@field:SerializedName("count")
	val count: Int? = null,

	@field:SerializedName("users")
	val users: List<ServerUsersItem?>? = null
)
