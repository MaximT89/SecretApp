package com.second.world.secretapp.core.bases

import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.second.world.secretapp.R
import com.second.world.secretapp.core.navigation.Navigator

abstract class BaseActivity : AppCompatActivity(), Navigator{

    override fun navigateTo(resId: Int, args: Bundle?, navOptions: NavOptions?) =
        findNavController(R.id.nav_host_fragment).navigate(resId, args, navOptions)

    override fun navigateTo(resId: Int, args: Bundle?) = findNavController(R.id.nav_host_fragment).navigate(resId, args)

    override fun navigateTo(resId: Int) = findNavController(R.id.nav_host_fragment).navigate(resId)

    override fun navigateUp() { findNavController(R.id.nav_host_fragment).navigateUp() }
    /**
     * Проверяем, если у нас отображается клавиатура, то при клике вне клавиатуры мы вызываем метод
     * [hideKeyboard] , которым скрываем клавиатуру
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) hideKeyboard()
        return super.dispatchTouchEvent(ev)
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(window.decorView.windowToken, 0)
    }
}