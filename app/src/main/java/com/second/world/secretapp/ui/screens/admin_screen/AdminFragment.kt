package com.second.world.secretapp.ui.screens.admin_screen

import androidx.fragment.app.viewModels
import com.second.world.secretapp.core.bases.BaseFragment
import com.second.world.secretapp.core.extension.click
import com.second.world.secretapp.core.extension.updateText
import com.second.world.secretapp.core.navigation.Destinations
import com.second.world.secretapp.databinding.FragmentAdminBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminFragment : BaseFragment<FragmentAdminBinding, AdminViewModel>(FragmentAdminBinding::inflate) {
    override val viewModel: AdminViewModel by viewModels()

    override fun initView() = with(binding){

        btnCopyToken.click {  clipToBuffer(viewModel.token.value.toString())  }

        btnCopySecretPin.click {  clipToBuffer(viewModel.secretPin.value.toString())  }

        btnBack.click { navigateTo(Destinations.ADMIN_TO_CALCULATOR.id) }

        btnClearToDefault.click { viewModel.clearAllToDefault()}
    }

    override fun initObservers() = with(viewModel) {

        token.observe { token ->
            updateText(binding.token, "token: $token")
        }

        secretPin.observe { secretPin ->
            updateText(binding.secretPin, "secretPin: $secretPin")
        }
    }
}