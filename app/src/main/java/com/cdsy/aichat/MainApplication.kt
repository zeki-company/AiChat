package com.cdsy.aichat

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.chibatching.kotpref.Kotpref
import com.chibatching.kotpref.gsonpref.gson
import com.google.gson.Gson
import com.cdsy.aichat.manager.di.koinModule
import com.cdsy.aichat.manager.finance.GoogleBillingManager
import com.cdsy.aichat.manager.sharedpref.SharedPrefModel
import com.cdsy.aichat.util.LocaleHelper
import io.reactivex.plugins.RxJavaPlugins
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import timber.log.Timber
import timber.log.Timber.DebugTree
import java.util.Locale


/**
 * @author Zeki
 * @version 1.0
 * @date 2019/10/11 18:10
 */

open class MainApplication : Application(), KodeinAware {

    companion object {
        var instance: MainApplication? = null
        var nowUserId:String = ""
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
}