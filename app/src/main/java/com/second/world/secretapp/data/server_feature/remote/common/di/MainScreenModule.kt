package com.second.world.secretapp.data.server_feature.remote.common.di

import com.second.world.secretapp.core.remote.MainRetrofitClient
import com.second.world.secretapp.data.server_feature.remote.common.api.MainScreenService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class MainScreenModule {

    @Provides
    fun provideMainScreenService(@MainRetrofitClient retrofit: Retrofit): MainScreenService =
        retrofit.create(MainScreenService::class.java)
}