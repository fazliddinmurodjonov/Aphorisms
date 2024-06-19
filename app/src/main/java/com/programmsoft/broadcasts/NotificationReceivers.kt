package com.programmsoft.broadcasts

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.programmsoft.utils.Functions

class NotificationReceivers : BroadcastReceiver() {
    override fun onReceive(context: Context?, p1: Intent?) {
        Functions.showNotification(context!!)
    }
}