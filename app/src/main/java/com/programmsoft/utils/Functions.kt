package com.programmsoft.utils

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavDeepLinkBuilder
import com.programmsoft.aphorisms.App
import com.programmsoft.aphorisms.MainActivity
import com.programmsoft.aphorisms.R
import com.programmsoft.aphorisms.databinding.SnackbarLayoutBinding
import com.programmsoft.broadcasts.NotificationReceivers
import com.programmsoft.fragments.DialogFragment
import com.programmsoft.models.AphorismItemResponse
import com.programmsoft.room.database.AphorismDB
import com.programmsoft.room.entity.Aphorism
import com.programmsoft.room.entity.AphorismCategory
import java.security.Permissions
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

object Functions {
    val db = AphorismDB.getInstance(App.instance)
    const val NOTIFICATION_CHANNEL = "Aphorisms"
    const val NOTIFICATION_ID = 100
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
        Toast.makeText(context, "Nusxa olindi.", Toast.LENGTH_SHORT).show()
    }


    @SuppressLint("InlinedApi")
    fun appNotifications(context: Context) {
        val intent = Intent()
        intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    fun isAllowNotifications(context: Context): Boolean {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        return notificationManager.areNotificationsEnabled()
    }

    fun showNotification(context: Context, aphorismId: Long) {
        val text = db.aphorismDao().getAphorismTextById(aphorismId)
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra("from_notification", true)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val bundle = Bundle().apply {
            putBoolean("from_notification", true)
        }
        val deepLink = NavDeepLinkBuilder(context).setGraph(R.navigation.aphorisms_nav)
            .setComponentName(MainActivity::class.java).setDestination(R.id.nav_home)
            .setArguments(bundle).createPendingIntent()
        val notificationBuilder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL)
            .setColor(ContextCompat.getColor(context, R.color.black_custom))
            .setSmallIcon(R.drawable.notification_logo)
            .setContentTitle(context.resources.getString(R.string.app_name))
            .setContentText(text)
            .setStyle(NotificationCompat.BigTextStyle().bigText(text))
            .setContentIntent(deepLink)
            .setPriority(NotificationCompat.PRIORITY_HIGH).setChannelId(NOTIFICATION_CHANNEL)
            .setAutoCancel(true)
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    fun setTimeOfAlarmManager(context: Context) {
        if (System.currentTimeMillis() > getDateInMilliseconds(getDate())) {
            setAlarmManager(
                1, context, getDateInMilliseconds(getDateAddedDay(1))
            )
        } else {
            setAlarmManager(
                1, context, getDateInMilliseconds(getDate())
            )
        }

    }

    private fun setAlarmManager(requestCode: Int, context: Context, time: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = createPendingIntent(context, requestCode)
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pendingIntent)
    }

    private fun createPendingIntent(context: Context, requestCode: Int): PendingIntent {
        val intent = Intent(context, NotificationReceivers::class.java)
        intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND)
        intent.putExtra("request_code", requestCode)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )
        } else {
            PendingIntent.getBroadcast(
                context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
    }

    fun createNotificationChannel(context: Context) {
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager ?: return
        val importance = NotificationManager.IMPORTANCE_HIGH
        val id = NOTIFICATION_CHANNEL
        val name = NOTIFICATION_CHANNEL
        val channel = NotificationChannel(id, name, importance)
        channel.enableLights(true)
        channel.lightColor = Color.BLUE
        channel.enableVibration(true)
        channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
        notificationManager.createNotificationChannel(channel)
    }

    fun getDateInMilliseconds(date: String): Long {
        val updatedDateTimeString = "11:25 $date"
        val format = SimpleDateFormat("HH:mm dd-MM-yyyy", Locale.ENGLISH)
        val d = format.parse(updatedDateTimeString)
        return d?.time ?: 0
    }

    fun getDate(): String {
        val currentDate = Date()
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
        return dateFormat.format(currentDate)
    }

    fun getDateAddedDay(addDay: Int): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, addDay)
        val nextDate = calendar.time
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
        return dateFormat.format(nextDate)
    }


}