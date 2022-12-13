package com.second.world.secretapp.ui.main_activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import com.second.world.secretapp.R
import com.second.world.secretapp.core.bases.BaseActivity
import com.second.world.secretapp.core.extension.*
import com.second.world.secretapp.core.navigation.Destinations
import com.second.world.secretapp.databinding.ActivityMainBinding
import com.second.world.secretapp.ui.screens.users_all_screen.model.TextSettingModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    companion object {
        const val TEXT_SETTING = "text_setting"
    }

    val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
        initObservers()
    }

    private fun initView() = with(binding) {

        binding.btnScreenSettings.setBackgroundResource(R.drawable.ic_baseline_menu_24)

        btnLogout.click {
            viewModel.logout()
            navigateTo(R.id.authFragment)
            openLeftMenu()
        }

        btnScreenSettings.click {
            openLeftMenu()
        }

        menuBtnUsers.click {
            navigateTo(R.id.usersAllFragment)
            openLeftMenu()
        }

        menuBtnMainScreen.click {
            navigateTo(R.id.mainFragment)
            openLeftMenu()
        }

        btnAddUser.click {
            navigateTo(
                Destinations.USERS_ALL_TO_USERS_ADD.id,
                bundleOf(TEXT_SETTING to viewModel.getTextSettings())
            )
        }

        btnCloseMenu.click {
            openLeftMenu()
        }

        btnBackOnFragment.click {
            navigateUp()
        }
    }

    fun clickBtnOffAllServer(btnOffAllServersClick: () -> Unit) {
        binding.btnOffAllServers.click {
            btnOffAllServersClick.invoke()
        }
    }

    fun showNotificationVersion(status: Boolean) {

        log(tag = "VERSION", message = "showNotificationVersion work and new status : $status")

        if (status) binding.notificationMainActivity.show()
        else binding.notificationMainActivity.hide()
    }

    private fun openLeftMenu() {
        if (binding.leftMenu.isVisible) {
            binding.leftMenu.hide()
            binding.navHostFragment.show()
            binding.btnScreenSettings.setBackgroundResource(R.drawable.ic_baseline_menu_24)
        } else {
            binding.leftMenu.show()
            binding.navHostFragment.hide()
            binding.btnScreenSettings.setBackgroundResource(R.drawable.ic_baseline_close_black_24)
        }
    }

    private fun initObservers() = with(viewModel) {}

    fun updateTitle(title: String) {
        updateText(binding.titleText, title)
    }

    fun saveTextSettings(data: TextSettingModel) {
        viewModel.saveTextSetting(data)
    }

    fun showBtnAddUser(visibility: Boolean) {
        if (visibility) binding.btnAddUser.show()
        else binding.btnAddUser.hide()
    }

    fun showBtnOffAllServer(visibility: Boolean) {
        if (visibility) binding.btnOffAllServers.show()
        else binding.btnOffAllServers.hide()
    }

    fun showTitleField(visibility: Boolean) {
        if (visibility) binding.titleField.show()
        else binding.titleField.hide()
    }

    fun showBtnBack(visibility: Boolean) {
        if (visibility) binding.btnBackOnFragment.show()
        else binding.btnBackOnFragment.hide()
    }

}