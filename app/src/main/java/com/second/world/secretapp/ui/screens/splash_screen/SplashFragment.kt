package com.second.world.secretapp.ui.screens.splash_screen

import androidx.fragment.app.viewModels
import com.second.world.secretapp.core.bases.BaseFragment
import com.second.world.secretapp.core.extension.click
import com.second.world.secretapp.core.navigation.Destinations
import com.second.world.secretapp.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment :
    BaseFragment<FragmentSplashBinding, SplashViewModel>(FragmentSplashBinding::inflate) {
    override val viewModel: SplashViewModel by viewModels()

    override fun initView()  {}

    override fun initObservers() {
        viewModel.isAuth.observe { isAuth ->
            if (isAuth) navigateTo(Destinations.SPLASH_TO_MAIN.id)
            else navigateTo(Destinations.SPLASH_TO_AUTH.id)
        }
    }
}