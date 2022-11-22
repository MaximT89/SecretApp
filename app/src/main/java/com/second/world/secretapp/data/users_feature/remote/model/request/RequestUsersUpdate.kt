package com.second.world.secretapp.data.users_feature.remote.model.request

import com.google.gson.annotations.SerializedName

data class RequestUsersUpdate(

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("active")
	val active: Boolean? = null
)
