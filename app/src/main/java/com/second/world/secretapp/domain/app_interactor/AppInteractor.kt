package com.second.world.secretapp.domain.app_interactor

import com.second.world.secretapp.BuildConfig
import com.second.world.secretapp.core.extension.log
import javax.inject.Inject

class AppInteractor @Inject constructor() {

    /**
     * Сравнение версии приложения
     */
    fun validateVersion(version : String) : Boolean {

        log(tag = "VERSION", message = "validateVersion is work")

        val validate = version == BuildConfig.VERSION_NAME

        log(tag = "VERSION", message = "validateVersion $validate")

        return version == BuildConfig.VERSION_NAME
    }

}