package com.second.world.secretapp.data.app.local

import com.second.world.secretapp.core.bases.BaseSharedPreferences
import com.second.world.secretapp.core.extension.editMe
import com.second.world.secretapp.core.extension.put
import javax.inject.Inject

class AppPrefs @Inject constructor(private val basePref: BaseSharedPreferences) {

    companion object {
        const val USER_IS_AUTH = "user_is_auth"
        const val DEFAULT_USER_IS_AUTH = false

        const val USER_SECRET_PIN = "user_secret_pin"
        const val DEFAULT_USER_SECRET_PIN = 555
    }

    fun loadUserIsAuth() =
        basePref.defaultPref().getBoolean(
            USER_IS_AUTH,
            DEFAULT_USER_IS_AUTH
        )

    fun saveUserIsAuth(value: Boolean) {
        basePref.defaultPref().editMe { it.put(USER_IS_AUTH to value) }
    }

    fun loadUserSecretPin() =
        basePref.defaultPref().getInt(
            USER_SECRET_PIN,
            DEFAULT_USER_SECRET_PIN
        )

    fun saveUserSecretPin(value: Int) {
        basePref.defaultPref().editMe { it.put(USER_SECRET_PIN to value) }
    }

}