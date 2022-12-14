package com.second.world.secretapp.ui.screens.main_screen

import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.second.world.secretapp.core.bases.BaseFragment
import com.second.world.secretapp.core.extension.hide
import com.second.world.secretapp.core.extension.show
import com.second.world.secretapp.core.navigation.Destinations
import com.second.world.secretapp.data.server_feature.remote.common.model.response.ResponseMainScreen
import com.second.world.secretapp.databinding.FragmentMainBinding
import com.second.world.secretapp.ui.main_activity.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment :
    BaseFragment<FragmentMainBinding, MainViewModel>(FragmentMainBinding::inflate) {
    override val viewModel: MainViewModel by viewModels()

    private val adapter = MainAdapter()

    override val showBtnOffAllServer : Boolean = true

    companion object {
        const val NEXT_SCREEN_CONN_KEY = "next_screen_conn_key"
    }

    override fun initView() = with(binding) {
        showTitle(true)

        mainRecyclerView.adapter = adapter

        (activity as MainActivity).clickBtnOffAllServer {

            alertDialog(titleAlert = "ВНИМАНИЕ!",
                bodyText = "ОТКЛЮЧИТЬ ВСЕ?",
                positiveBtnLogic = {

                    alertDialog(titleAlert = "ВНИМАНИЕ!",
                        bodyText = "ВЫ УВЕРЕНЫ?",
                        positiveBtnLogic = {
                            viewModel.disableAllServers()
                        })

                }
            )
        }
    }

    override fun initCallbacks() {

        adapter.callBackBtnStopServer = { serverData ->
            alertDialog(
                titleAlert = "Предупреждение",
                bodyText = "Подтвердите действие",
                positiveBtnLogic = {
                    viewModel.clickRedBtn(serverData)
                })
        }

        adapter.callBackBtnGoServerUsers = { nextScreenConn ->
            navigateTo(Destinations.MAIN_TO_SERVER_USERS.id, bundleOf(
                NEXT_SCREEN_CONN_KEY to nextScreenConn
            ))
        }
    }

    override fun initObservers() = with(viewModel) {
        listServerClients.observe { listServers ->
            val sortList = listServers?.sortedBy { it?.connUi?.sort }
            adapter.submitList(sortList)
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

                is MainScreenState.VersionValidateState -> {
                    showNotificationVersion(state.showNotification)
                }

                is MainScreenState.Test -> {
                    showSnackbar(state.testText)
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()

        viewModel.stopTimer()
    }

    override fun onResume() {
        super.onResume()

        viewModel.startPingServers()
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