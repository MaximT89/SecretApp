package com.second.world.secretapp.data.users_feature.remote.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class ResponseUsersAll(

	@field:SerializedName("result")
	val result: Boolean? = null,

	@field:SerializedName("data")
	val data: DataUsersAll? = null,

	@field:SerializedName("error")
	val error: List<String?>? = null,

	@field:SerializedName("version")
	val version: String? = null
)

@Parcelize
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
) : Parcelable

data class Text(

	@field:SerializedName("title_all_users_page")
	val titleAllUsersPage: String? = null,

	@field:SerializedName("add_user_button")
	val addUserBtnText: String? = null,

	@field:SerializedName("name_user_button")
	val nameUserBtnText: String? = null,

	@field:SerializedName("phone_user_button")
	val phoneUserBtnText: String? = null,

	@field:SerializedName("save_user_button")
	val saveUserBtnText: String? = null,

	@field:SerializedName("title_add_user_page")
	val titleAddUsersPage: String? = null,

	@field:SerializedName("title_update_user_page")
	val titleUpdateUsersPage: String? = null,
)

data class DataUsersAll(

	@field:SerializedName("text")
	val text: Text? = null,

	@field:SerializedName("users")
	val users: List<UsersItem?>? = null
)
