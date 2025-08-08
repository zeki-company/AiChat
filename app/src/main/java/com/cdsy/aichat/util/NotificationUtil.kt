package com.cdsy.aichat.util

import android.app.Notification.DEFAULT_ALL
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.app.PendingIntent
import android.content.Context.NOTIFICATION_SERVICE
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import com.cdsy.aichat.MainApplication
import com.cdsy.aichat.R
import com.cdsy.aichat.ui.main.MainActivity

/**
 * @author Zeki
 * @version 1.0
 * @date 2019/10/11 18:10
 */

const val CHANNEL_ID_STRING = "channel_notification"

fun sendNotification(context: Context, title: String, contentString: String) {
    val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager?
    val contentIntent = PendingIntent.getActivity(
        context,
        0,
        Intent(
            context,
            MainActivity::class.java
        ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK),
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    //适配8.0service
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val mChannel =
            NotificationChannel(CHANNEL_ID_STRING, "美社", NotificationManager.IMPORTANCE_HIGH)
        notificationManager?.createNotificationChannel(mChannel)
        notificationManager?.notify(
            Math.random().toInt(), NotificationCompat.Builder(
                MainApplication.instance!!,
                CHANNEL_ID_STRING
            ).setContentTitle(title)
                .setContentText(contentString)
                .setWhen(System.currentTimeMillis())
                //.setSmallIcon(R.drawable.logo)
                //.setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.logo))
                //.setFullScreenIntent(contentIntent,true)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setTicker("悬浮通知")
                .setDefaults(DEFAULT_ALL)
                .setPriority(NotificationManager.IMPORTANCE_HIGH)
                .build()
        )
    } else {
        notificationManager?.notify(
            Math.random().toInt(), NotificationCompat.Builder(MainApplication.instance!!)
                .setContentTitle(title)
                .setContentText(contentString)
                .setWhen(System.currentTimeMillis())
                //.setSmallIcon(R.drawable.logo)
                //.setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.logo))
                //.setFullScreenIntent(contentIntent,true)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setTicker("悬浮通知")
                .setDefaults(DEFAULT_ALL)
                .setPriority(NotificationManager.IMPORTANCE_HIGH)
                .build()
        )
    }

}
