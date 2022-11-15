package com.second.world.secretapp.ui.screens

import android.opengl.Visibility
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
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
        btnLogout.click {
            viewModel.logout()
        }
    }

    private fun initObservers()= with(viewModel){
        userIsAuth.observe(this@MainActivity) { isAuth ->
            if (!isAuth) findNavController(R.id.nav_host_fragment).navigate(R.id.authFragment)
        }
    }


    fun updateTitle(title : String) {
        updateText(binding.titleText, title)
    }

    fun showTitleField(visibility: Boolean){
        if (visibility) binding.titleField.show()
        else binding.titleField.hide()
    }

}