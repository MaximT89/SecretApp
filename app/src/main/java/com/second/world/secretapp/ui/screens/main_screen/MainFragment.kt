package com.second.world.secretapp.ui.screens.main_screen

import androidx.fragment.app.viewModels
import com.second.world.secretapp.core.bases.BaseFragment
import com.second.world.secretapp.core.extension.click
import com.second.world.secretapp.core.extension.hide
import com.second.world.secretapp.core.extension.show
import com.second.world.secretapp.data.main_screen.remote.common.model.response.ResponseMainScreen
import com.second.world.secretapp.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment :
    BaseFragment<FragmentMainBinding, MainViewModel>(FragmentMainBinding::inflate) {
    override val viewModel: MainViewModel by viewModels()

    private val adapter = MainAdapter()

    override fun initView() = with(binding) {
        showTitle(true)

        mainRecyclerView.adapter = adapter

        btnPing.click {
            viewModel.pingAllConnItem()
        }
    }

    override fun initObservers() = with(viewModel) {

        adapter.callBackBtnStopServer = { item ->

            alertDialog(
                titleAlert = "Предупреждение",
                bodyText = "Подтвердите действие",
                positiveBtnLogic = {
                    viewModel.clickRedBtn(item)
                })
        }


        listConn.observe {
            adapter.submitList(it)
        }

        mainScreenState.observe { state ->
            when (state) {
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

                is MainScreenState.ErrorRedBtn -> {
                    progressBar()
                    showSnackbar(state.messageError)
                }

                is MainScreenState.SuccessRedBtn -> {
                    progressBar()
                    showSnackbar(state.data.toString())
                }
            }
        }
    }

    private fun showUi(data: ResponseMainScreen) {
        updateTitle(data.data?.settings?.titleName!!)
    }

    private fun showError(messageError: String) {
        showSnackbar(messageError)
    }

    private fun progressBar(progress: Boolean = false) = with(binding) {
        if (progress) progressBar.show()
        else progressBar.hide()
    }
}