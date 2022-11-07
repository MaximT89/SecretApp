package com.second.world.secretapp.core.navigation

import com.second.world.secretapp.R

enum class Destinations(val id : Int) {
    CALCULATOR_TO_AUTH(R.id.action_calculatorFragment_to_authFragment),
    CALCULATOR_TO_MAIN(R.id.action_calculatorFragment_to_mainFragment),

    MAIN_TO_AUTH(R.id.action_mainFragment_to_authFragment),

    AUTH_TO_MAIN(R.id.action_authFragment_to_mainFragment)
}