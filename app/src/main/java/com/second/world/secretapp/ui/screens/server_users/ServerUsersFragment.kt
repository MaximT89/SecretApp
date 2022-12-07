package com.second.world.secretapp.ui.screens.server_users

import androidx.fragment.app.viewModels
import com.second.world.secretapp.core.bases.BaseFragment
import com.second.world.secretapp.core.extension.hide
import com.second.world.secretapp.core.extension.show
import com.second.world.secretapp.databinding.FragmentServerUsersBinding
import com.second.world.secretapp.ui.screens.main_screen.MainFragment
import com.second.world.secretapp.ui.screens.main_screen.model_ui.NextScreenConnUI
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ServerUsersFragment :
    BaseFragment<FragmentServerUsersBinding, ServerUsersViewModel>(FragmentServerUsersBinding::inflate) {
    override val viewModel: ServerUsersViewModel by viewModels()

    override fun initView() = with(binding) {
        showTitle(true)

        updateTitle(title = "Пользователи")
    }

    override fun initObservers() = with(viewModel) {

        serverUsersState.observe { state ->
            when(state){
                ServerUsersState.EmptyState -> { updateUi(progress = true) }
                is ServerUsersState.Error -> { updateUi() }
                ServerUsersState.Loading -> { updateUi(progress = true) }
                is ServerUsersState.NoInternet -> { updateUi() }
                is ServerUsersState.Success -> { updateUi(content = true) }
                is ServerUsersState.Test -> { updateUi() }
            }
        }
    }

    private fun updateUi(progress: Boolean = false, content : Boolean = false) = with(binding){
        if(progress) progressBar.show()
        else progressBar.hide()

        if(content) recyclerViewServerUsers.show()
        else recyclerViewServerUsers.hide()
    }

    override fun listenerBundleArguments() {

        readArguments<NextScreenConnUI>(MainFragment.NEXT_SCREEN_CONN_KEY,
            ifExist = { connItem ->
                viewModel.getUsers(connItem)
            })

    }
}
