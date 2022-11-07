package com.second.world.secretapp.ui.screens.auth_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.second.world.secretapp.core.bases.BaseResult
import com.second.world.secretapp.core.bases.BaseViewModel
import com.second.world.secretapp.core.bases.Dispatchers
import com.second.world.secretapp.data.auth.remote.model.ResponseAuth
import com.second.world.secretapp.data.auth.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val dispatchers: Dispatchers,
) : BaseViewModel() {

    private var _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

//    fun getSms(phone: String) {
//        dispatchers.launchBackground(viewModelScope) {
//            when (val result = repository.getSms(phone)) {
//                is BaseResult.Error -> {
//                    if (result.err.code == 1) getSms(phone)
//                    else if (result.err.code != 0)
//                        _authState.postValue(AuthState.Error(result.err.message))
//                    else
//                        _authState.postValue(AuthState.NoInternet(result.err.message))
//                }
//                is BaseResult.Success -> _authState.postValue(
//                    AuthState.SuccessGetSms(
//                        result.data
//                    )
//                )
//            }
//        }
//    }
}

sealed class AuthState {
    class SuccessGetSms(val data: ResponseAuth) : AuthState()
    object SuccessAuth : AuthState()
    class Error(val massageError: String) : AuthState()
    class NoInternet(val massageError: String) : AuthState()
    object Loading : AuthState()
}