package com.second.world.secretapp.ui.screens.users_all_screen

import androidx.fragment.app.viewModels
import com.second.world.secretapp.core.bases.BaseFragment
import com.second.world.secretapp.core.extension.hide
import com.second.world.secretapp.core.extension.show
import com.second.world.secretapp.data.users_feature.remote.model.response.ResponseUsersAll
import com.second.world.secretapp.data.users_feature.remote.model.response.UsersItem
import com.second.world.secretapp.databinding.FragmentUsersAllBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UsersAllFragment :
    BaseFragment<FragmentUsersAllBinding, UsersAllViewModel>(FragmentUsersAllBinding::inflate) {
    override val viewModel: UsersAllViewModel by viewModels()

    private val adapter = UsersAllAdapter()

    override val showBtnAddUser = true

    override fun initView() = with(binding) {

        showTitle(true)

        recyclerView.adapter = adapter
    }

    override fun initObservers() = with(viewModel) {

        usersAllState.observe { state ->

            when (state) {
                is UsersAllStates.Error -> showUi(showError = true, errorText = state.messageError)
                UsersAllStates.Loading -> showUi(showProgress = true)
                is UsersAllStates.NoInternet -> showUi(showError = true, errorText = state.messageError)
                is UsersAllStates.Success -> showUi(showListUsers = true, response = state.data)
            }
        }
    }

    private fun showUi(
        showProgress: Boolean = false,
        showError: Boolean = false,
        errorText: String = "",
        showListUsers: Boolean = false,
        response : ResponseUsersAll? = null
    ) = with(binding) {

        if (showProgress) progressBar.show()
        else progressBar.hide()

        if (showError) {
            this.errorText.show()
            this.errorText.text = errorText
        } else this.errorText.hide()

        if (showListUsers) recyclerView.show()
        else recyclerView.hide()

        if(response != null){
            val data : List<UsersItem?>? = response.data?.users
            adapter.items = data
        }
    }
}