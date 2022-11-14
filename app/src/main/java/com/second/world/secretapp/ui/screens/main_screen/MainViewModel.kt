package com.second.world.secretapp.ui.screens.main_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.second.world.secretapp.core.bases.BaseResult
import com.second.world.secretapp.core.bases.BaseViewModel
import com.second.world.secretapp.core.bases.Dispatchers
import com.second.world.secretapp.core.remote.Failure
import com.second.world.secretapp.data.app.local.AppPrefs
import com.second.world.secretapp.data.main_screen.remote.model.response.ResponseMainScreen
import com.second.world.secretapp.data.main_screen.repository.MainScreenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val appPrefs: AppPrefs,
    private val dispatchers: Dispatchers,
    private val repository: MainScreenRepository
) : BaseViewModel() {

    private val _userIsAuth = MutableLiveData<Boolean>()
    val userIsAuth: LiveData<Boolean> = _userIsAuth

    private val _mainScreenState = MutableLiveData<MainScreenState>()
    val mainScreenState: LiveData<MainScreenState> = _mainScreenState

    init {
        getIsAuth()
        getMainScreenUi()
    }

    private fun getMainScreenUi() {

        _mainScreenState.value = MainScreenState.Loading

        dispatchers.launchBackground(viewModelScope) {

            val result: BaseResult<ResponseMainScreen, Failure> =
                repository.getMainScreenSettings(appPrefs.loadAppLang()!!)

            when (result) {
                is BaseResult.Error -> errorResult(result)
                is BaseResult.Success -> _mainScreenState.postValue(MainScreenState.Success(result.data))
            }
        }
    }

    private fun errorResult(result: BaseResult.Error<Failure>) {
        if (result.err.code == 1) getMainScreenUi()
        else if (result.err.code != 0)
            _mainScreenState.postValue(MainScreenState.Error(result.err.message))
        else
            _mainScreenState.postValue(MainScreenState.NoInternet(result.err.message))
    }

    private fun getIsAuth() {
        _userIsAuth.value = appPrefs.loadUserIsAuth()
    }

    private fun saveIsAuth(status: Boolean) {
        appPrefs.saveUserIsAuth(status)
    }

    fun logout() {
        saveIsAuth(false)
        getIsAuth()
    }
}

sealed class MainScreenState {

    // Успешный запрос данных для страницы
    class Success(val data: ResponseMainScreen) : MainScreenState()

    // Состояние ошибки
    class Error(val messageError: String) : MainScreenState()

    // Ошибка когда отсутствует интернет
    class NoInternet(val messageError: String) : MainScreenState()

    // Загрузка при каких то долгих запросах
    object Loading : MainScreenState()
}
