package com.second.world.secretapp.data.auth.repository

import com.second.world.secretapp.core.bases.BaseResult
import com.second.world.secretapp.core.remote.Failure
import com.second.world.secretapp.data.app.local.AppPrefs
import com.second.world.secretapp.data.auth.remote.AuthRemoteDataSource
import com.second.world.secretapp.data.auth.remote.model.ResponseAuth
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val remoteDataSource: AuthRemoteDataSource,
    private val appPrefs: AppPrefs
) {

//    suspend fun getSms(phoneNumber : String) : BaseResult<ResponseAuth, Failure> {
//        return remoteDataSource.getSms()
//    }

    fun saveToken(token: String) {
        appPrefs.saveTokenApi(token)
    }


}