package com.second.world.secretapp.ui.main_activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.second.world.secretapp.R
import com.second.world.secretapp.core.bases.BaseActivity
import com.second.world.secretapp.core.extension.click
import com.second.world.secretapp.core.extension.hide
import com.second.world.secretapp.core.extension.show
import com.second.world.secretapp.core.extension.updateText
import com.second.world.secretapp.core.navigation.Destinations
import com.second.world.secretapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    val binding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel : MainActivityViewModel by viewModels()

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
            navigateTo(Destinations.USERS_ALL_TO_USERS_ADD.id)
        }
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

    private fun initObservers()= with(viewModel){

    }


    fun updateTitle(title : String) {
        updateText(binding.titleText, title)
    }

    fun showBtnAddUser(visibility: Boolean){
        if(visibility) binding.btnAddUser.show()
        else binding.btnAddUser.hide()
    }

    fun showTitleField(visibility: Boolean){
        if (visibility) binding.titleField.show()
        else binding.titleField.hide()
    }

}