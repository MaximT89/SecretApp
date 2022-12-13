package com.second.world.secretapp.domain.server_users_screen

import com.second.world.secretapp.core.extension.log
import com.second.world.secretapp.ui.screens.main_screen.model_ui.NextScreenConnUI
import javax.inject.Inject

class ServerUsersInteractor @Inject constructor() {

    fun constractBaseUrlServerUsers(coonUi : NextScreenConnUI) : String {

        log("CONNECTION BASE URL: ${coonUi.protocol}://${coonUi.ip}:${coonUi.port}/")

        return "${coonUi.protocol}://${coonUi.ip}:${coonUi.port}/"
    }


}