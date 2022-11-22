package com.second.world.secretapp.data.users_feature.remote.model.request

import com.google.gson.annotations.SerializedName

data class RequestUsersAdd(

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("name")
	val name: String? = null
)
