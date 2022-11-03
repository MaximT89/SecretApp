package com.second.world.secretapp.ui.screens.main_screen

import androidx.fragment.app.viewModels
import com.second.world.secretapp.core.bases.BaseFragment
import com.second.world.secretapp.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding, MainViewModel>(FragmentMainBinding::inflate) {
    override val viewModel: MainViewModel by viewModels()

    override fun initView() {

    }

    override fun initObservers() {

    }

}