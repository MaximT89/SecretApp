package com.second.world.secretapp.ui.screens.auth_screen

import androidx.fragment.app.viewModels
import com.second.world.secretapp.core.bases.BaseFragment
import com.second.world.secretapp.databinding.FragmentAuthBinding
import dagger.hilt.android.AndroidEntryPoint
import ru.tinkoff.decoro.MaskImpl
import ru.tinkoff.decoro.parser.UnderscoreDigitSlotsParser
import ru.tinkoff.decoro.watchers.FormatWatcher
import ru.tinkoff.decoro.watchers.MaskFormatWatcher

@AndroidEntryPoint
class AuthFragment : BaseFragment<FragmentAuthBinding, AuthViewModel>(FragmentAuthBinding::inflate) {
    override val viewModel: AuthViewModel by viewModels()

    override fun initView() = with(binding){

//        btnToMain.click{
//            log(phoneEditText.text.toString().filter { it.isDigit() })
//        }

        val slots = UnderscoreDigitSlotsParser().parseSlots("(___) ___ __-__")
        val formatWatcher: FormatWatcher = MaskFormatWatcher(MaskImpl.createTerminated(slots))
        formatWatcher.installOn(phoneEditText)
    }

    override fun initObservers() {

//        viewModel.authState.observe { state ->
//            when(state){
//                is AuthState.Error -> {
//
//                }
//                AuthState.Loading -> {
//
//                }
//                is AuthState.NoInternet -> {
//
//                }
//                is AuthState.SuccessAuth -> {
//
//                }
//
//                else -> {}
//            }
//        }
    }
}