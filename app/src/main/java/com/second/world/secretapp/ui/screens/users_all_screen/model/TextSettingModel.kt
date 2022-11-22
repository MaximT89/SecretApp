package com.second.world.secretapp.ui.screens.users_all_screen.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class TextSettingModel(
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
) : Parcelable