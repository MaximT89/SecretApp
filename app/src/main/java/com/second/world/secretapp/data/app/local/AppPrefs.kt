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

        const val TOKEN_API = "token_api"
        const val DEFAULT_TOKEN_API = ""

        const val APP_LANG = "app_lang"
        const val DEFAULT_APP_LANG = "ru"
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

    fun loadTokenApi() =
        basePref.defaultPref().getString(
            TOKEN_API,
            DEFAULT_TOKEN_API
        )

    fun saveTokenApi(value: String) {
        basePref.defaultPref().editMe { it.put(TOKEN_API to value) }
    }

    fun loadAppLang() =
        basePref.defaultPref().getString(
            APP_LANG,
            DEFAULT_APP_LANG
        )

    fun saveAppLang(value: String) {
        basePref.defaultPref().editMe { it.put(APP_LANG to value) }
    }

}