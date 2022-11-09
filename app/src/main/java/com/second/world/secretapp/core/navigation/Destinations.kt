package com.second.world.secretapp.core.navigation

import com.second.world.secretapp.R

enum class Destinations(val id : Int) {
    CALCULATOR_TO_AUTH(R.id.action_calculatorFragment_to_authFragment),
    CALCULATOR_TO_MAIN(R.id.action_calculatorFragment_to_mainFragment),
    CALCULATOR_TO_ADMIN(R.id.action_calculatorFragment_to_adminFragment),

    MAIN_TO_AUTH(R.id.action_mainFragment_to_authFragment),

    AUTH_TO_MAIN(R.id.action_authFragment_to_mainFragment),

    ADMIN_TO_CALCULATOR(R.id.action_adminFragment_to_calculatorFragment)
}