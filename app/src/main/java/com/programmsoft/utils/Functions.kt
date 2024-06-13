package com.programmsoft.utils

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.view.View
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.FragmentManager
import com.programmsoft.aphorisms.App
import com.programmsoft.aphorisms.R
import com.programmsoft.fragments.DialogFragment
import com.programmsoft.models.AphorismItemResponse
import com.programmsoft.room.database.AphorismDB
import com.programmsoft.room.entity.Aphorism
import com.programmsoft.room.entity.AphorismCategory

object Functions {
    val db = AphorismDB.getInstance(App.instance)
    val fragmentList = setOf(
        R.id.nav_home,
        R.id.nav_menu,
        R.id.nav_bookmark,
        R.id.nav_settings,
        R.id.fragment_search,
        R.id.fragment_category
    )
    val destinationsWithoutBottomNav =
        listOf(R.id.nav_settings, R.id.fragment_search, R.id.fragment_category)
    private val categoryList = listOf(
        "Aql"
    )
    val categoryEngList = listOf(
        "intelligence"
    )

    fun insertCategories() {
        categoryList.zip(categoryEngList).forEach { (categoryUzb, categoryEng) ->
            val categories = AphorismCategory(nameEng = categoryEng, nameUzb = categoryUzb)
            db.aphorismCategoryDao().insert(categories)
        }
    }

    fun connectivityManager(context: Context) {
        val connectivityManager =
            context.getSystemService(ConnectivityManager::class.java) as ConnectivityManager
        connectivityManager.requestNetwork(
            ConnectivityManagers.networkRequest, ConnectivityManagers.networkCallback
        )
    }

    fun appearanceStatusNavigationBars(
        window: Window,
        mode: Int,
        appearanceLightStatusBars: Boolean,
        appearanceNavigationBars: Boolean
    ) {
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

    fun insertData(aphorismResponse: AphorismItemResponse) {
        val existAphorism = db.aphorismDao().isAphorismExist(aphorismResponse.id)
        if (existAphorism == 0) {
            val aphorism = Aphorism()
            aphorism.aphorismId = aphorismResponse.id
            aphorism.categoryId = categoryList.indexOf(aphorismResponse.caption) + 1
            aphorism.news = aphorismResponse.isNew
            aphorism.text = aphorismResponse.text
            aphorism.bookmark = 0
            db.aphorismDao().insert(aphorism)
        }
    }

    fun showDialogWithArgument(fragmentManager: FragmentManager, factId: Long) {
        val dialog = DialogFragment()
        val id = bundleOf("aphorism_id" to factId)
        dialog.arguments = id
        dialog.show(fragmentManager, "aphorism")
    }
}