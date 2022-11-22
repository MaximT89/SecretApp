package com.second.world.secretapp.ui.screens.users_add_screen

import androidx.fragment.app.viewModels
import com.second.world.secretapp.core.bases.BaseFragment
import com.second.world.secretapp.core.extension.click
import com.second.world.secretapp.core.extension.hide
import com.second.world.secretapp.core.extension.onlyDigits
import com.second.world.secretapp.core.extension.show
import com.second.world.secretapp.data.users_feature.remote.model.response.ResponseUsersAdd
import com.second.world.secretapp.data.users_feature.remote.model.response.ResponseUsersAll
import com.second.world.secretapp.data.users_feature.remote.model.response.UsersItem
import com.second.world.secretapp.databinding.FragmentUsersAddBinding
import com.second.world.secretapp.ui.main_activity.MainActivity
import com.second.world.secretapp.ui.screens.users_all_screen.UsersAllStates
import com.second.world.secretapp.ui.screens.users_all_screen.model.TextSettingModel
import dagger.hilt.android.AndroidEntryPoint
import ru.tinkoff.decoro.MaskImpl
import ru.tinkoff.decoro.parser.UnderscoreDigitSlotsParser
import ru.tinkoff.decoro.watchers.FormatWatcher
import ru.tinkoff.decoro.watchers.MaskFormatWatcher

@AndroidEntryPoint
class UsersAddFragment :
    BaseFragment<FragmentUsersAddBinding, UsersAddViewModel>(FragmentUsersAddBinding::inflate) {
    override val viewModel: UsersAddViewModel by viewModels()

    override fun initView() = with(binding) {
        createPhoneMask()

        btnCreateUser.click {
            viewModel.addUser(
                nameEditText.text.toString(),
                phoneEditText.text.toString().onlyDigits()
            )
        }
    }

    override fun initObservers() = with(viewModel) {
        usersAddState.observe { state ->

            when (state) {
                is UsersAddStates.Error -> updateUi(
                    showContent = true,
                    showError = true,
                    errorText = state.messageError
                )
                UsersAddStates.Loading -> updateUi(showProgress = true)
                is UsersAddStates.NoInternet -> updateUi(
                    showContent = true,
                    showError = true,
                    errorText = state.messageError
                )
                is UsersAddStates.Success -> {
                    updateUi(showContent = true)
                    showSnackbar("Пользователь добавлен")
                    navigateUp()
                }
            }
        }
    }

    private fun updateUi(
        showProgress: Boolean = false,
        showError: Boolean = false,
        errorText: String = "",
        showContent: Boolean = false,
    ) = with(binding) {

        if (showProgress) progressBar.show()
        else progressBar.hide()

        if (showError) {
            this.textError.show()
            this.textError.text = errorText
        } else this.textError.hide()

        if (showContent) content.show()
        else content.hide()
    }

    override fun listenerBundleArguments() {

        readArguments<TextSettingModel>(MainActivity.TEXT_SETTING,
            ifExist = { textSettings ->
                updateTexts(textSettings)
            }
        )
    }

    private fun updateTexts(textSettings: TextSettingModel) = with(binding) {
        btnCreateUser.text = textSettings.addUserBtnText

        userPhoneField.hint = textSettings.phoneUserBtnText
        userNameField.hint = textSettings.nameUserBtnText

        textSettings.titleAddUsersPage?.let { updateTitle(title = it) }
    }

    private fun createPhoneMask() {
        val slots = UnderscoreDigitSlotsParser().parseSlots("(___) ___ __-__")
        val formatWatcher: FormatWatcher = MaskFormatWatcher(MaskImpl.createTerminated(slots))
        formatWatcher.installOn(binding.phoneEditText)
    }
}