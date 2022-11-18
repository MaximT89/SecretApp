package com.second.world.secretapp.data.main_screen.remote.conn_elements.di

import com.second.world.secretapp.core.remote.ConnRetrofitClient
import com.second.world.secretapp.data.main_screen.remote.conn_elements.api.ConnService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class ConnModule {

    @Provides
    fun provideConnApi(@ConnRetrofitClient retrofit: Retrofit): ConnService =
        retrofit.create(ConnService::class.java)
}