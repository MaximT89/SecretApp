package com.second.world.secretapp.ui.screens.users_detail_screen

import android.annotation.SuppressLint
import androidx.fragment.app.viewModels
import com.second.world.secretapp.core.bases.BaseFragment
import com.second.world.secretapp.core.extension.*
import com.second.world.secretapp.data.users_feature.remote.model.response.UsersItem
import com.second.world.secretapp.databinding.FragmentUsersUpdateBinding
import com.second.world.secretapp.ui.screens.users_all_screen.UsersAllFragment
import com.second.world.secretapp.ui.screens.users_all_screen.model.TextSettingModel
import dagger.hilt.android.AndroidEntryPoint
import ru.tinkoff.decoro.MaskImpl
import ru.tinkoff.decoro.parser.UnderscoreDigitSlotsParser
import ru.tinkoff.decoro.watchers.FormatWatcher
import ru.tinkoff.decoro.watchers.MaskFormatWatcher

@SuppressLint("SetTextI18n")
@AndroidEntryPoint
class UsersUpdateFragment :
    BaseFragment<FragmentUsersUpdateBinding, UsersUpdateViewModel>(FragmentUsersUpdateBinding::inflate) {
    override val viewModel: UsersUpdateViewModel by viewModels()

    override val showBtnBack: Boolean = true

    override fun initView() = with(binding) {
        createPhoneMask()

        btnStartEdit.click { viewModel.contentEnabled(true) }

        btnCancelEdit.click { viewModel.contentEnabled(false) }

        btnSaveEdit.click { updateUserDataUi() }

        btnDeleteUser.click {
            alertDialog(titleAlert = "Удаление",
                bodyText = "Вы действительно хотите удалить?",
                positiveBtnLogic = {
                    viewModel.deleteUser()
                })
        }
    }

    private fun updateUserDataUi() = with(binding){
        viewModel.updateUserFromServer(
            name = nameEditText.text.toString(),
            phone = phoneEditText.text.toString().onlyDigits(),
            active = activeCheckBox.isChecked
        )
    }

    private fun contentEnable(status: Boolean) = with(binding) {
        if (status) {
            nameEditText.enabled()
            phoneEditText.enabled()
            activeCheckBox.enabled()
        } else {
            nameEditText.notEnabled()
            phoneEditText.notEnabled()
            activeCheckBox.notEnabled()
        }
    }

    override fun initObservers() = with(viewModel) {

        userData.observe { userData ->
            updateUi(userData)
        }

        pageTextSettings.observe { pageTexts ->
            updateTexts(pageTexts)
        }

        usersUpdateState.observe { state ->
            when (state) {

                UsersUpdateStates.EditOff -> {
                    contentEnable(false)
                    showUi(showBtnEdit = true)
                }

                UsersUpdateStates.EditOn -> {
                    contentEnable(true)
                    showUi(showBtnsSave = true)
                }

                is UsersUpdateStates.Error -> {
                    contentEnable(false)
                    showUi(showBtnEdit = true, showError = true, errorMessage = state.messageError)
                }

                UsersUpdateStates.Loading -> showProgress()

                is UsersUpdateStates.NoInternet -> {
                    showUi(showError = true, errorMessage = state.messageError)
                    contentEnabled(false)
                }

                is UsersUpdateStates.Success -> {
                    contentEnable(false)
                    showUi(showBtnEdit = true)
                    showSnackbar("Пользователь обновлен")
                }

                is UsersUpdateStates.SuccessDeleteUser -> {
                    showSnackbar("Успешно удалено")
                    navigateUp()
                }

                is UsersUpdateStates.PhoneNotCorrect -> {
                    contentEnable(true)
                    showUi(showBtnsSave = true, showBtnEdit = false, showError = true, errorMessage = state.messageError)
                }
            }
        }
    }

    private fun showProgress() {
        binding.progressBar.show()
    }

    private fun showUi(
        progress: Boolean = false,
        showBtnEdit: Boolean = false,
        showBtnsSave: Boolean = false,
        showError: Boolean = false,
        errorMessage: String = ""
    ) = with(binding) {

        if (progress) progressBar.show()
        else progressBar.hide()

        if (showBtnsSave) btnSaveField.show()
        else btnSaveField.hide()

        if (showBtnEdit) btnStartEdit.show()
        else btnStartEdit.hide()

        if (showError) errorText.show()
        else errorText.hide()

        errorText.text = errorMessage
    }

    override fun listenerBundleArguments() {

        readArguments<UsersItem?>(UsersAllFragment.USER_DATA,
            ifExist = { data -> viewModel.updateUserData(data) })

        readArguments<TextSettingModel>(UsersAllFragment.PAGE_TEXT_SETTINGS,
            ifExist = { data -> viewModel.savePageSettings(data) })
    }

    private fun updateTexts(pageTexts: TextSettingModel?) = with(binding) {
        pageTexts?.titleUpdateUsersPage?.let { updateTitle(it) }
        btnStartEdit.text = pageTexts?.titleUpdateUsersPage

        userNameField.hint = pageTexts?.nameUserBtnText
        userPhoneField.hint = pageTexts?.phoneUserBtnText
    }

    private fun updateUi(data: UsersItem?) = with(binding) {
        nameEditText.setText(data?.name)
        phoneEditText.setText(data?.phone)

        activeCheckBox.isChecked = data?.active == 1
    }

    private fun createPhoneMask() {
        val slots = UnderscoreDigitSlotsParser().parseSlots("(___) ___ __-__")
        val formatWatcher: FormatWatcher = MaskFormatWatcher(MaskImpl.createTerminated(slots))
        formatWatcher.installOn(binding.phoneEditText)
    }
}