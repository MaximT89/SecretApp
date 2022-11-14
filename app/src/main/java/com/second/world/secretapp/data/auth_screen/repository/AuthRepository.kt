package com.second.world.secretapp.data.auth_screen.repository

import com.second.world.secretapp.core.bases.BaseResult
import com.second.world.secretapp.core.remote.Failure
import com.second.world.secretapp.data.app.local.AppPrefs
import com.second.world.secretapp.data.auth_screen.remote.source.AuthCloudDataSource
import com.second.world.secretapp.data.auth_screen.remote.model.requests.RequestGetSms
import com.second.world.secretapp.data.auth_screen.remote.model.requests.RequestGetUserData
import com.second.world.secretapp.data.auth_screen.remote.model.responses.ResponseGetSms
import com.second.world.secretapp.data.auth_screen.remote.model.responses.ResponseGetUserData
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val remoteDataSource: AuthCloudDataSource,
    private val appPrefs: AppPrefs
) {

    suspend fun getSms(request : RequestGetSms) : BaseResult<ResponseGetSms, Failure> {
        return remoteDataSource.getSms(request)
    }

    suspend fun getUserData(request : RequestGetUserData) : BaseResult<ResponseGetUserData, Failure> {
        return remoteDataSource.getUserData(request)
    }

    fun saveToken(token: String) {
        appPrefs.saveTokenApi(token)
    }

    fun getToken() : String = appPrefs.loadTokenApi()!!

    fun loadUserIsAuth() : Boolean = appPrefs.loadUserIsAuth()

    fun saveUserIsAuth(status : Boolean) {
        appPrefs.saveUserIsAuth(status)
    }

    fun loadLang() : String = appPrefs.loadAppLang()!!

    fun loadUserSecretPin() : Int = appPrefs.loadUserSecretPin()

    fun saveUserSecretPin(pin : Int) {
        appPrefs.saveUserSecretPin(pin)
    }
}