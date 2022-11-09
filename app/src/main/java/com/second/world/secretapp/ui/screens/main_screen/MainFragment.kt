package com.second.world.secretapp.ui.screens.main_screen

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.second.world.secretapp.R
import com.second.world.secretapp.core.bases.BaseFragment
import com.second.world.secretapp.core.extension.click
import com.second.world.secretapp.core.extension.hide
import com.second.world.secretapp.core.extension.show
import com.second.world.secretapp.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding, MainViewModel>(FragmentMainBinding::inflate) {
    override val viewModel: MainViewModel by viewModels()

    override fun initView() = with(binding) {

        btnScreenSettings.click { showSetting(true) }

        mainScreenRoot.click { showSetting(false) }

        btnCloseSettings.click { showSetting(false) }
    }

    private fun showSetting(status: Boolean) {
        if (status) {
            binding.settingRoot.show()
            binding.mainScreenRoot.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.gray))
        } else {
            binding.settingRoot.hide()
            binding.mainScreenRoot.setBackgroundColor(0)
        }
    }

    override fun initObservers() {

    }
}