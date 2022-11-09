package com.second.world.secretapp.core.remote

import com.second.world.secretapp.data.app.local.AppPrefs
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class TokenInterceptor @Inject constructor(private val appPrefs : AppPrefs) : Interceptor{

    override fun intercept(chain: Interceptor.Chain): Response {

        val token = appPrefs.loadTokenApi()

        val newRequest = chain
            .request()
            .newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        return chain.proceed(newRequest)
    }
}