package com.second.world.secretapp.core.remote

import java.io.IOException

class NoInternetConnectionException : IOException() {
    override val message: String = "You are offline"
}