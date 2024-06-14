package com.programmsoft.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.FragmentManager
import com.programmsoft.aphorisms.App
import com.programmsoft.aphorisms.R
import com.programmsoft.aphorisms.databinding.SnackbarLayoutBinding
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
        listOf(R.id.fragment_search, R.id.fragment_category)
    private val categoryUzbList = listOf(
        "Aql"
    )
    private val categoryEngList = listOf(
        "intelligence"
    )

    fun insertCategories() {
        categoryUzbList.zip(categoryEngList).forEach { (categoryUzb, categoryEng) ->
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
            aphorism.categoryId = categoryUzbList.indexOf(aphorismResponse.caption) + 1
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

    fun showDialog(fragmentManager: FragmentManager, tag: String) {
        val dialog = DialogFragment()
        dialog.show(fragmentManager, tag)
    }

    fun rateApp(context: Context) {
        val playStoreUrl = "https://play.google.com/store/apps/details?id=" + context.packageName
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(playStoreUrl))
        context.startActivity(browserIntent)
    }

    fun otherApps(context: Context) {
        val url = context.resources.getString(R.string.google_play)
        val uriUrl: Uri = Uri.parse(url)
        val launchBrowser = Intent(Intent.ACTION_VIEW, uriUrl)
        context.startActivity(launchBrowser)
    }

    fun shareApp(context: Context) {
        val intent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT, "Aforizmlar" +
                        "\n" +
                        "\uD83D\uDC47Dasturni yuklab olish\uD83D\uDC47\n" +
                        "https://play.google.com/store/apps/details?id=com.programmsoft.aphorisms"
            )
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(intent, null)
        context.startActivity(shareIntent)
    }
    fun sendData(context: Context, data: String) {
        val sendIntent = Intent(Intent.ACTION_VIEW)
        sendIntent.putExtra("sms_body", data)
        sendIntent.type = "vnd.android-dir/mms-sms"
        context.startActivity(sendIntent)
    }

    fun shareData(context: Context, data: String) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, data)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        context.startActivity(shareIntent)
    }

    fun copyData(context: Context, data: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip: ClipData = ClipData.newPlainText("label", data)
        clipboard.setPrimaryClip(clip)
        val toastLayout: SnackbarLayoutBinding =
            SnackbarLayoutBinding.inflate(LayoutInflater.from(App.instance), null, false)
        val toast = Toast(App.instance)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = toastLayout.root
        toast.setGravity(Gravity.BOTTOM or Gravity.FILL_HORIZONTAL, 0, 0)
        toast.show()
    }
}