package com.cdsy.aichat

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.cdsy.aichat.manager.appsflyer.AppsflyerWrapper
import com.chibatching.kotpref.Kotpref
import com.chibatching.kotpref.gsonpref.gson
import com.cdsy.aichat.manager.di.koinModule
import com.cdsy.aichat.manager.finance.GoogleBillingManager
import com.cdsy.aichat.manager.sharedpref.SharedPrefModel
import com.cdsy.aichat.push.AppFirebaseMessagingService
import com.cdsy.aichat.util.LocaleHelper
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import io.reactivex.plugins.RxJavaPlugins
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import timber.log.Timber
import timber.log.Timber.DebugTree


/**
 * @author Zeki
 * @version 1.0
 * @date 2019/10/11 18:10
 */

open class MainApplication : Application(), KodeinAware {

    companion object {
        var instance: MainApplication? = null
        var nowUserId:String = ""
        private var firebaseAnalytics: FirebaseAnalytics? = null
        private class CrashReportingTree : Timber.Tree() {
            override fun log(
                priority: Int,
                tag: String?,
                message: String,
                t: Throwable?
            ) {
            }
        }
    }

    override val kodein by Kodein.lazy {
        import(androidXModule(this@MainApplication))
        import(koinModule(this@MainApplication))
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        initKotPref()
        initFirebase()
        AppsflyerWrapper.getInstance().initAppsflyer(this)
        initLanguage()
        initTimber()
        GoogleBillingManager.init()
        RxJavaPlugins.setErrorHandler { e: Throwable? -> e?.printStackTrace() }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    //初始化本地SharedPref框架
    private fun initKotPref() {
        Kotpref.init(this)
        Kotpref.gson = Gson()
    }

    //初始化语言设置
    private fun initLanguage() {
        val savedLanguageCode = SharedPrefModel.languageCode
        if (!savedLanguageCode.isNullOrEmpty()) {
            LocaleHelper.setLocale(this, savedLanguageCode)
        }
    }

    //日志框架初始化
    private fun initTimber() {
        if (BuildConfig.DEBUG) Timber.plant(DebugTree())
        else Timber.plant(CrashReportingTree())
    }

    /**
     * 对应旧项目 AppApplication.initAnalytics():
     * - 初始化 FirebaseAnalytics
     * - 启用 FCM 自动生成注册令牌
     * - FCM token 的持久化交给 AppFirebaseMessagingService.onNewToken -> SharedPrefModel.deviceToken
     */
    private fun initFirebase() {
        firebaseAnalytics = FirebaseAnalytics.getInstance(this).apply {
            setAnalyticsCollectionEnabled(true)
        }
        // 自动初始化 FirebaseMessaging，以便尽早触发 token 下发
        FirebaseMessaging.getInstance().isAutoInitEnabled = true

        // 主动拉取一次当前 FCM token，避免某些机型上 onNewToken 触发时机较晚
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) return@addOnCompleteListener
                val token = task.result ?: return@addOnCompleteListener
                SharedPrefModel.deviceToken = token
            }
    }
}