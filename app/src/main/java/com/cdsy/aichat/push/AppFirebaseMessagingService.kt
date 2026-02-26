package com.cdsy.aichat.push

import android.util.Log
import com.cdsy.aichat.manager.sharedpref.SharedPrefModel
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber

/**
 * 对应旧项目的 MyFirebaseMessagingService：
 * - 负责接收 FCM token，并保存到 SharedPrefModel
 * - 后续登录时作为 deviceInfo.fcmToken 传给后端
 */
class AppFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.d("[Firebase][Push]", "onNewToken($token)")

        // 按我们自己的 SharedPrefModel 方案持久化 FCM token
        SharedPrefModel.deviceToken = token
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Timber.d("[Firebase][Push]", "onMessageReceived from=${remoteMessage.from}")
        // 这里暂时只做日志，后续如果需要通知逻辑可以再补
    }
}

