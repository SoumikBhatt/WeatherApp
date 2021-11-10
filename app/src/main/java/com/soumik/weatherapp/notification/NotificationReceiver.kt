package com.soumik.weatherapp.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log

class NotificationReceiver : BroadcastReceiver() {

    companion object {
        private const val TAG = "NotificationReceiver"
    }

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        Log.d(TAG, "onReceive: Triggered")

        val i = Intent(context,NotificationService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                context.startForegroundService(i)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            try {
                context.startService(i)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }
}