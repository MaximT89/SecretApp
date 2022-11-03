package com.second.world.secretapp.core.navigation

import com.second.world.secretapp.R

enum class Destinations(val id : Int) {
    SPLASH_TO_MAIN(R.id.action_splashFragment_to_mainFragment),
    SPLASH_TO_AUTH(R.id.action_splashFragment_to_authFragment),

    MAIN_TO_AUTH(R.id.action_mainFragment_to_authFragment),

    AUTH_TO_MAIN(R.id.action_authFragment_to_mainFragment)
}