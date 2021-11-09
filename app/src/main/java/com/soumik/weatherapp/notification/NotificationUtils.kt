package com.soumik.weatherapp.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import com.soumik.weatherapp.R
import com.soumik.weatherapp.ui.home.ui.HomeActivity
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


class NotificationUtils(_context: Context) : ContextWrapper(_context) {

    companion object {
        const val CHANNEL_ID = "notification_channel"
        const val TIMELINE_CHANNEL_NAME = "Weather Info"
    }

    private var _notificationManager: NotificationManager? = null

    private val dismissIntent= Intent(_context,HomeActivity::class.java)

    private val pendingIntent = PendingIntent.getActivity(_context, 0,dismissIntent,PendingIntent.FLAG_CANCEL_CURRENT)


    fun setNotification(title: String?, body: String?, imageUrl:String?): NotificationCompat.Builder {

        val bitmap = getBitmapFromURL(imageUrl!!)

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setLargeIcon(bitmap)
            .setContentTitle(title)
            .setContentText(body).setStyle(NotificationCompat.BigTextStyle()
                .bigText(body))
            .setAutoCancel(true)
            .setVibrate(longArrayOf(100, 200, 300, 400))
            .setContentIntent(pendingIntent)
            .setFullScreenIntent(pendingIntent,true)
            .setPriority(NotificationCompat.PRIORITY_MAX)

    }

    /**
     * Downloading push notification image before displaying it in
     * the notification tray
     */
    private fun getBitmapFromURL(strURL: String): Bitmap? {
        return try {
            val url = URL(strURL)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }

    }

    private fun createChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                TIMELINE_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = ""
                enableLights(true)
                enableVibration(true)
                lightColor= Color.BLUE
                vibrationPattern= longArrayOf(100, 200, 300, 400)
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC

            }
            manager!!.createNotificationChannel(channel)
        }
    }

    val manager: NotificationManager?
        get() {
            if (_notificationManager == null) {
                _notificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            }
            return _notificationManager
        }

    init {

        dismissIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        createChannel()
    }
}