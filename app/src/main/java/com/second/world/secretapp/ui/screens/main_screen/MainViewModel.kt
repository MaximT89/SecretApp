package com.second.world.secretapp.ui.screens.main_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.second.world.secretapp.core.bases.BaseResult
import com.second.world.secretapp.core.bases.BaseViewModel
import com.second.world.secretapp.core.bases.Dispatchers
import com.second.world.secretapp.core.remote.Failure
import com.second.world.secretapp.data.app.local.AppPrefs
import com.second.world.secretapp.data.main_screen.remote.common.model.response.ResponseMainScreen
import com.second.world.secretapp.data.main_screen.repository.MainScreenRepository
import com.second.world.secretapp.ui.screens.main_screen.model_ui.MapperConnDataToUI
import com.second.world.secretapp.ui.screens.main_screen.model_ui.SrvItemUi
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val appPrefs: AppPrefs,
    private val dispatchers: Dispatchers,
    private val repository: MainScreenRepository,
    private val connMapper: MapperConnDataToUI
) : BaseViewModel() {

    private val _mainScreenState = MutableLiveData<MainScreenState>()
    val mainScreenState: LiveData<MainScreenState> = _mainScreenState

    private val _listConn = MutableLiveData<List<SrvItemUi?>?>()
    val listConn: LiveData<List<SrvItemUi?>?> = _listConn

    init {
        getMainScreenUi()
    }

    /**
     * 1. Сперва получить весь список КНОПОК, смапить их в другой тип данных где нужно добавить id элементам
     * 2. Записать кнопки в лив дату
     * 3. После записи вызвать метод, где в цикле будут вызываться запросы для каждого элемента
     * 4. Для каждого элемента получить данные и по id заменить данные
     */

    private fun getMainScreenUi() {

        _mainScreenState.value = MainScreenState.Loading

        dispatchers.launchBackground(viewModelScope) {

            val result: BaseResult<ResponseMainScreen, Failure> =
                repository.getMainScreenSettings(appPrefs.loadAppLang()!!)

            when (result) {
                is BaseResult.Error -> errorResult(result)
                is BaseResult.Success -> successResponseGetMainScreen(result.data)
            }
        }
    }

    /**
     * Обработка корректного ответа от сервера
     */
    private fun successResponseGetMainScreen(data: ResponseMainScreen) {

        if(data.result!!){
            _mainScreenState.postValue(MainScreenState.Success(data))
            _listConn.postValue(connMapper.map(data.data?.srv))

            pingAllConnItem()

        } else _mainScreenState.postValue(MainScreenState.Error("Произошла ошибка"))
    }

    /**
     * В данном методе мы должны пингануть все объекты из присланных
     */
    private fun pingAllConnItem() {



    }

    private fun errorResult(result: BaseResult.Error<Failure>) {
        if (result.err.code == 1) getMainScreenUi()
        else if (result.err.code != 0)
            _mainScreenState.postValue(MainScreenState.Error(result.err.message))
        else
            _mainScreenState.postValue(MainScreenState.NoInternet(result.err.message))
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
