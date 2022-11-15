package com.second.world.secretapp.ui.screens.main_screen

import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.second.world.secretapp.R
import com.second.world.secretapp.core.bases.BaseFragment
import com.second.world.secretapp.core.extension.click
import com.second.world.secretapp.core.extension.hide
import com.second.world.secretapp.core.extension.show
import com.second.world.secretapp.core.extension.updateText
import com.second.world.secretapp.core.navigation.Destinations
import com.second.world.secretapp.data.main_screen.remote.model.response.ResponseMainScreen
import com.second.world.secretapp.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment :
    BaseFragment<FragmentMainBinding, MainViewModel>(FragmentMainBinding::inflate) {
    override val viewModel: MainViewModel by viewModels()

    override fun initView() = with(binding) {

        btnScreenSettings.click {
            if (leftMenu.isVisible) leftMenu.hide()
            else leftMenu.show()
        }

        mainScreenRoot.click {

        }

        btnLogout.click {
            viewModel.logout()
        }
    }

    override fun initObservers() = with(viewModel) {
        userIsAuth.observe { isAuth ->
            if (!isAuth) navigateTo(Destinations.MAIN_TO_AUTH.id)
        }

        mainScreenState.observe { state ->
            when(state){
                is MainScreenState.Error -> {
                    progressBar()
                    showError(state.messageError)
                }
                MainScreenState.Loading -> {
                    progressBar(progress = true)
                }
                is MainScreenState.NoInternet -> {
                    progressBar()
                    showError(state.messageError)
                }
                is MainScreenState.Success -> {
                    progressBar()
                    showUi(state.data)
                }
            }
        }
    }

    private fun showUi(data: ResponseMainScreen) = with(binding) {
        updateText(titleText, data.data?.settings?.titleName!!)



        
    }

    private fun showError(messageError: String) {
        showSnackbar(messageError)
    }

    private fun progressBar(progress: Boolean = false) = with(binding){
        if(progress) progressBar.show()
        else progressBar.hide()
    }
}