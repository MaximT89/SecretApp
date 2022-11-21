package com.second.world.secretapp.ui.screens.users_add_screen

import androidx.fragment.app.viewModels
import com.second.world.secretapp.core.bases.BaseFragment
import com.second.world.secretapp.databinding.FragmentUsersAddBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UsersAddFragment : BaseFragment<FragmentUsersAddBinding, UsersAddViewModel>(FragmentUsersAddBinding::inflate) {
    override val viewModel: UsersAddViewModel by viewModels()

    override fun initView() {
    }

    override fun initObservers() {
    }
}