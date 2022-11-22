package com.second.world.secretapp.data.users_feature.remote.di

import com.second.world.secretapp.core.remote.MainRetrofitClient
import com.second.world.secretapp.data.users_feature.remote.api.UsersAddService
import com.second.world.secretapp.data.users_feature.remote.api.UsersAllService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class UsersModule {

    @Provides
    fun provideUsersAllApi(@MainRetrofitClient retrofit: Retrofit): UsersAllService =
        retrofit.create(UsersAllService::class.java)

    @Provides
    fun provideUsersAddApi(@MainRetrofitClient retrofit: Retrofit): UsersAddService =
        retrofit.create(UsersAddService::class.java)
}