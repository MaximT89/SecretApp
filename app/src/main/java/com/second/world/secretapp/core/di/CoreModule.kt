package com.second.world.secretapp.core.di

import com.second.world.secretapp.core.bases.BaseSharedPreferences
import com.second.world.secretapp.core.bases.Dispatchers
import com.second.world.secretapp.core.common.ResourceProvider
import com.second.world.secretapp.core.remote.NetworkInterceptor
import com.second.world.secretapp.core.remote.ResponseWrapper
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CoreModule {

    @Binds
    abstract fun bindResponseWrapper(responseWrapper: ResponseWrapper.Impl) : ResponseWrapper

    @Binds
    abstract fun bindDispatchers(dispatchers : Dispatchers.Impl) : Dispatchers

    @Binds
    abstract fun bindResourceProvider(provider : ResourceProvider.Impl) : ResourceProvider

    @Binds
    abstract fun bindNetworkInterceptor(interceptor: NetworkInterceptor.Impl) : NetworkInterceptor

    @Binds
    abstract fun bindSharedPreferences(prefs : BaseSharedPreferences.Impl) : BaseSharedPreferences
}