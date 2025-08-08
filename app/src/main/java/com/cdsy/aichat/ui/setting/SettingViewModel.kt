package com.cdsy.aichat.ui.setting

import android.app.Application
import com.cdsy.aichat.MainApplication
import com.cdsy.aichat.manager.api.GoogleSignInService
import com.cdsy.aichat.manager.sharedpref.SharedPrefModel
import com.cdsy.aichat.ui.base.BaseViewModel
import com.cdsy.aichat.ui.base.Event
import com.cdsy.aichat.ui.base.LiveEvent
import com.cdsy.aichat.util.switchThread
import java.util.Locale

class SettingViewModel(application: Application) : BaseViewModel(application) {
    private val googleSignInService = GoogleSignInService(application)

    val signOutEvent = LiveEvent<Event<Boolean>>()
    val languageChangeEvent = LiveEvent<Event<String>>()

    fun signOut() {
        googleSignInService.signOut()
            .switchThread()
            .doOnSuccess { _: Unit ->
                // 清除用户信息
                MainApplication.nowUserId = ""
                SharedPrefModel.userId = ""
                signOutEvent.postValue(Event(true))
            }
            .bindLife()
    }

    fun changeLanguage(languageCode: String) {
        // 保存语言设置到SharedPreferences
        SharedPrefModel.languageCode = languageCode
        languageChangeEvent.postValue(Event(languageCode))
    }

    fun getCurrentLanguageCode(): String {
        return SharedPrefModel.languageCode ?: Locale.getDefault().language
    }
}