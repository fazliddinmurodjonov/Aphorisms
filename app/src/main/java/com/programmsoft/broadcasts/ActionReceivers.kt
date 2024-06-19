package com.programmsoft.broadcasts

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.programmsoft.utils.Functions

class ActionReceivers : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                Intent.ACTION_BOOT_COMPLETED,
                Intent.ACTION_BATTERY_CHANGED,
                Intent.ACTION_MY_PACKAGE_REPLACED,
                "android.intent.action.TIME_SET" -> {
                    Functions.setTimeOfAlarmManager(context!!)
                }
                else -> {
                    Functions.setTimeOfAlarmManager(context!!)
                }
            }
    }
}
