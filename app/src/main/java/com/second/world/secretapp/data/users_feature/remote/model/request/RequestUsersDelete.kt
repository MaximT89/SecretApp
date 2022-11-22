package com.second.world.secretapp.data.users_feature.remote.model.request

import com.google.gson.annotations.SerializedName

data class RequestUsersDelete(

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("lang")
	val lang: String? = null
)
