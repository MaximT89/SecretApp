package com.second.world.secretapp.data.auth_screen.remote.di

import com.second.world.secretapp.data.auth_screen.remote.api.AuthService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class AuthModule {

    @Provides
    fun provideAuthService(retrofit: Retrofit) : AuthService = retrofit.create(AuthService::class.java)
}