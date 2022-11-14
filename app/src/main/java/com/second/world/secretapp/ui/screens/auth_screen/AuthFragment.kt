package com.second.world.secretapp.ui.screens.auth_screen

import androidx.fragment.app.viewModels
import com.second.world.secretapp.core.bases.BaseFragment
import com.second.world.secretapp.core.extension.click
import com.second.world.secretapp.core.extension.hide
import com.second.world.secretapp.core.extension.onlyDigits
import com.second.world.secretapp.core.extension.show
import com.second.world.secretapp.core.navigation.Destinations
import com.second.world.secretapp.databinding.FragmentAuthBinding
import dagger.hilt.android.AndroidEntryPoint
import ru.tinkoff.decoro.MaskImpl
import ru.tinkoff.decoro.parser.UnderscoreDigitSlotsParser
import ru.tinkoff.decoro.watchers.FormatWatcher
import ru.tinkoff.decoro.watchers.MaskFormatWatcher

@AndroidEntryPoint
class AuthFragment :
    BaseFragment<FragmentAuthBinding, AuthViewModel>(FragmentAuthBinding::inflate) {
    override val viewModel: AuthViewModel by viewModels()

    override fun initView() {

        createPhoneMask()

        // телефон будет получен в формате 9536506580
        // Запрос смс кода
        binding.btnGetSms.click {
            viewModel.saveUserPhone(binding.phoneEditText.text.toString().onlyDigits())
            viewModel.getSms(binding.phoneEditText.text.toString().onlyDigits())
        }

        // Сверка смс кода (отправка кода на сервер и получение результата)
        binding.btnCheckSms.click {
            viewModel.getUserData(binding.smsEditText.text.toString())
        }

        // Обновить секретный код
        binding.btnUpdateNewPin.click {
            viewModel.updateNewPin(
                binding.firstNewPin.text.toString().onlyDigits(),
                binding.secondNewPin.text.toString().onlyDigits()
            )
        }
    }

    override fun initObservers() = with(binding) {

        viewModel.authState.observe { state ->
            when (state) {
                is AuthState.Error -> {
                    showError(visible = true, message = state.messageError)
                    progressBar.hide()
                }
                AuthState.Loading -> {
                    showRoot(progress = true)
                    showError()
                }
                is AuthState.NoInternet -> {
                    showError(visible = true, message = state.messageError)
                }
                is AuthState.SuccessAuth -> {
                    showError()
                    navigateTo(Destinations.AUTH_TO_MAIN.id)
                }
                is AuthState.SuccessGetSms -> {
                    showRoot(smsRoot = true, phoneRoot = false)
                    showError()
                }
                AuthState.ChangeSecretPin -> {
                    showRoot(changeSecretPinRoot = true, phoneRoot = false)
                    showError()
                }
            }
        }
    }

    private fun showRoot(
        smsRoot: Boolean = false,
        phoneRoot: Boolean = true,
        progress: Boolean = false,
        changeSecretPinRoot: Boolean = false
    ) {
        if (smsRoot) binding.smsRoot.show()
        else binding.smsRoot.hide()

        if (phoneRoot) binding.phoneRoot.show()
        else binding.phoneRoot.hide()

        if (changeSecretPinRoot) binding.changeSecretPinRoot.show()
        else binding.changeSecretPinRoot.hide()

        if (progress) binding.progressBar.show()
        else binding.progressBar.hide()
    }

    private fun showError(visible: Boolean = false, message: String = "") {
        if (visible) binding.errorText.show()
        else binding.errorText.hide()

        binding.errorText.text = message
    }

    private fun createPhoneMask() {
        val slots = UnderscoreDigitSlotsParser().parseSlots("(___) ___ __-__")
        val formatWatcher: FormatWatcher = MaskFormatWatcher(MaskImpl.createTerminated(slots))
        formatWatcher.installOn(binding.phoneEditText)
    }
}