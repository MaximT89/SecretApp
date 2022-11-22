package com.second.world.secretapp.data.users_feature.remote.di

import com.second.world.secretapp.core.remote.MainRetrofitClient
import com.second.world.secretapp.data.users_feature.remote.api.UsersAddService
import com.second.world.secretapp.data.users_feature.remote.api.UsersAllService
import com.second.world.secretapp.data.users_feature.remote.api.UsersDeleteService
import com.second.world.secretapp.data.users_feature.remote.api.UsersUpdateService
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

    @Provides
    fun provideUsersUpdateApi(@MainRetrofitClient retrofit: Retrofit): UsersUpdateService =
        retrofit.create(UsersUpdateService::class.java)

    @Provides
    fun provideUsersDeleteApi(@MainRetrofitClient retrofit: Retrofit): UsersDeleteService =
        retrofit.create(UsersDeleteService::class.java)
}