package com.second.world.secretapp.data.main_screen.remote.di

import com.second.world.secretapp.data.main_screen.remote.api.MainScreenService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class MainScreenModule {

    @Provides
    fun provideMainScreenService(retrofit: Retrofit): MainScreenService =
        retrofit.create(MainScreenService::class.java)
}