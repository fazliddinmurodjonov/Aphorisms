package com.programmsoft.utils

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.view.View
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat

object Functions {
    fun connectivityManager(context: Context) {
        val connectivityManager =
            context.getSystemService(ConnectivityManager::class.java) as ConnectivityManager
        connectivityManager.requestNetwork(
            ConnectivityManagers.networkRequest, ConnectivityManagers.networkCallback
        )
    }

    fun appearanceStatusNavigationBars(window: Window, mode: Int,appearanceLightStatusBars:Boolean,appearanceNavigationBars:Boolean) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = true
        }
        if (mode == 0) {
            val decorView: View = window.decorView
            val wic = WindowInsetsControllerCompat(window, decorView)
            wic.isAppearanceLightStatusBars = appearanceLightStatusBars
            wic.isAppearanceLightNavigationBars = appearanceNavigationBars
        } else {
            val decorView: View = window.decorView
            val wic = WindowInsetsControllerCompat(window, decorView)
            wic.isAppearanceLightStatusBars = appearanceLightStatusBars
            wic.isAppearanceLightNavigationBars = appearanceNavigationBars
        }
    }
    fun statusNavigationBarsColor(
        window: Window,
        context: Context,
        colorStatus: Int,
        colorNavigation: Int
    ) {
        window.statusBarColor = ContextCompat.getColor(context, colorStatus)
        window.navigationBarColor = ContextCompat.getColor(context, colorNavigation)
    }
}