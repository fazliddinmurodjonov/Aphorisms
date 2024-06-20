package com.programmsoft.broadcasts

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.programmsoft.utils.Functions
import com.programmsoft.utils.SharedPreference

class NotificationReceivers : BroadcastReceiver() {
    override fun onReceive(context: Context?, p1: Intent?) {
        SharedPreference.init(context!!)
        if (SharedPreference.isAllowNotification) {
            if (Functions.db.aphorismDao()
                    .isAphorismExistById(SharedPreference.lastAphorismId) != 0
            ) {
                Functions.showNotification(context, SharedPreference.lastAphorismId)
                SharedPreference.lastAphorismId += 1
            } else {
                SharedPreference.lastAphorismId = 1
                Functions.showNotification(context, SharedPreference.lastAphorismId)
            }
            Functions.setTimeOfAlarmManager(context)
        }

    }
}