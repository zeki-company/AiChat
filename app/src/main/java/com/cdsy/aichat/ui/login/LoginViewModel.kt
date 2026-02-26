package com.cdsy.aichat.ui.login

import android.app.Application
import android.content.Intent
import com.cdsy.aichat.BuildConfig
import com.cdsy.aichat.MainApplication
import com.cdsy.aichat.manager.api.GoogleSignInService
import com.cdsy.aichat.manager.api.LoginService
import com.cdsy.aichat.manager.appsflyer.AppsflyerWrapper
import com.cdsy.aichat.manager.sharedpref.SharedPrefModel
import com.cdsy.aichat.model.api.login.CaptchaSendEmailRequest
import com.cdsy.aichat.model.api.login.CaptchaSendPhoneRequest
import com.cdsy.aichat.model.api.login.DeviceInfo
import com.cdsy.aichat.model.api.login.LoginRequest
import com.cdsy.aichat.ui.base.BaseViewModel
import com.cdsy.aichat.ui.base.Event
import com.cdsy.aichat.ui.base.LiveEvent
import com.cdsy.aichat.util.DeviceUtil
import com.cdsy.aichat.util.switchThread
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import io.reactivex.Observable
import java.util.concurrent.TimeUnit
import org.kodein.di.generic.instance
import java.util.Locale

class LoginViewModel(application: Application) : BaseViewModel(application) {
    private val loginService by instance<LoginService>()
    private val googleSignInService = GoogleSignInService(application)

    val googleSignInIntentEvent = LiveEvent<Event<Intent>>()
    val googleSignInSuccessEvent = LiveEvent<Event<Boolean>>()
    val googleSignInErrorEvent = LiveEvent<Event<String>>()

    // Email / Phone 验证码与登录事件
    val emailCaptchaEvent = LiveEvent<Event<Pair<String, String>>>() // email, key
    val phoneCaptchaEvent =
        LiveEvent<Event<Triple<String, String, String>>>() // phone, callCode, key
    val loginSuccessEvent = LiveEvent<Event<Unit>>()
    val appsFlyerReadyEvent = LiveEvent<Event<Unit>>()

    /**
     * 构造登录时需要的 deviceInfo 字段，等价旧项目 DeviceEntity。
     */
    private fun buildDeviceInfo(): DeviceInfo {
        val context = getApplication<Application>()
        val locale = Locale.getDefault()
        val language = "${locale.language}-${locale.country}" // 如 zh-TW

        return DeviceInfo(
            imei = DeviceUtil.getUniqueId(context),
            platform = 2,
            os = "Android ${DeviceUtil.getOsVersion()}",
            hardware = DeviceUtil.getDeviceName(),
            fcmToken = SharedPrefModel.deviceToken,
            packageVersion = BuildConfig.VERSION_NAME,
            packageBuild = BuildConfig.VERSION_CODE.toString(),
            packageBundleId = context.packageName,
            language = language
        )
    }

    fun startGoogleSignIn() {
        progressDialog.postValue(true)
        val signInIntent = googleSignInService.getSignInIntent()
        googleSignInIntentEvent.postValue(Event(signInIntent))
    }

    /**
     * 通过 RxJava 轮询 AppsFlyerManager.APP_USER_SOURCE，准备好后发事件，配合全局转圈
     */
    fun waitAppsFlyerReady() {
        Observable.interval(100, TimeUnit.MILLISECONDS)
            .take(100) // 最多 5 秒
            .filter { AppsflyerWrapper.APP_USER_SOURCE.isNotEmpty() }
            .firstElement()
            .switchThread()
            .doOnSubscribe { progressDialog.postValue(true) }
            .doFinally { progressDialog.postValue(false) }
            .doOnSuccess {
                appsFlyerReadyEvent.postValue(Event(Unit))
            }
            .doOnError {
                // 超时或异常，直接认为可以继续，不抛到 UI
                appsFlyerReadyEvent.postValue(Event(Unit))
            }.bindLife()
    }

    /**
     * Google SDK 登录成功后，按照旧项目的逻辑：
     * - 拿到 idToken
     * - 调用统一的 v1/login 接口，source = GOOGLE
     * 不再走新项目示例里的 auth/google/login。
     */
    fun handleGoogleSignInResult(data: Intent?) {
        var googleAccount: GoogleSignInAccount? = null

        googleSignInService.handleSignInResult(data)
            .flatMap { account: GoogleSignInAccount ->
                googleAccount = account
                val idToken = account.idToken.orEmpty()
                if (idToken.isEmpty()) {
                    return@flatMap io.reactivex.Single.error<com.cdsy.aichat.model.api.ResultModel<*>>(
                        Throwable("Google idToken is empty")
                    ) as io.reactivex.Single<com.cdsy.aichat.model.api.ResultModel<com.cdsy.aichat.model.api.login.LoginResult>>
                }

                val request = LoginRequest(
                    source = "GOOGLE",
                    afSource = AppsflyerWrapper.APP_USER_SOURCE,
                    afType = 0,
                    deviceInfo = buildDeviceInfo(),
                    token = idToken
                )
                loginService.login(request)
            }
            .switchThread()
            .catchApiError()
            .doOnSubscribe { progressDialog.postValue(true) }
            .doFinally { progressDialog.postValue(false) }
            .doOnSuccess { result ->
                val login = result.data
                val user = login.user
                val tokenInfo = login.token

                val uid = (user?.id ?: 0L).toString()
                SharedPrefModel.userId = uid
                MainApplication.nowUserId = uid

                SharedPrefModel.setUserModel {
                    token = tokenInfo?.token.orEmpty()
                    portrait = googleAccount?.photoUrl?.toString().orEmpty()
                    name = googleAccount?.displayName.orEmpty()
                }
                googleSignInSuccessEvent.postValue(Event(true))
            }
            .doOnError { exception: Throwable ->
                googleSignInErrorEvent.postValue(Event(exception.message ?: "Google登录失败"))
            }
            .bindLife()
    }

    fun signOut() {
        googleSignInService.signOut()
            .switchThread()
            .doOnSuccess {
                // 清除用户信息
                SharedPrefModel.userId = ""
            }
            .bindLife()
    }

    /**
     * 发送邮箱验证码
     */
    fun sendEmailCaptcha(email: String) {
        loginService.sendEmailCaptcha(
            CaptchaSendEmailRequest(email = email, type = 0)
        )
            .switchThread()
            .catchApiError()
            .doOnSubscribe { progressDialog.postValue(true) }
            .doFinally { progressDialog.postValue(false) }
            .doOnSuccess { result ->
                val key = result.data.key.orEmpty()
                emailCaptchaEvent.postValue(Event(email to key))
            }
            .bindLife()
    }

    /**
     * 发送手机验证码
     */
    fun sendPhoneCaptcha(callCode: String, phone: String) {
        loginService.sendPhoneCaptcha(
            CaptchaSendPhoneRequest(phone = phone, callCode = callCode, type = 0)
        )
            .switchThread()
            .catchApiError()
            .doOnSubscribe { progressDialog.postValue(true) }
            .doFinally { progressDialog.postValue(false) }
            .doOnSuccess { result ->
                val key = result.data.key.orEmpty()
                phoneCaptchaEvent.postValue(Event(Triple(phone, callCode, key)))
            }
            .bindLife()
    }

    /**
     * 通过邮箱验证码登录
     */
    fun loginByEmail(key: String, code: String) {
        val token = "$key.$code"
        val request = LoginRequest(
            token = token,
            source = "EMAIL",
            afSource = AppsflyerWrapper.APP_USER_SOURCE,
            afType = 0,
            deviceInfo = buildDeviceInfo()
        )
        loginService.login(request)
            .switchThread()
            .catchApiError()
            .doOnSubscribe { progressDialog.postValue(true) }
            .doFinally { progressDialog.postValue(false) }
            .doOnSuccess {
                loginSuccessEvent.postValue(Event(Unit))
            }
            .bindLife()
    }

    /**
     * 通过手机验证码登录
     */
    fun loginByPhone(key: String, code: String) {
        val token = "$key.$code"
        val request = LoginRequest(
            token = token,
            source = "PHONE",
            afSource = AppsflyerWrapper.APP_USER_SOURCE,
            afType = 0,
            deviceInfo = buildDeviceInfo()
        )
        loginService.login(request)
            .switchThread()
            .catchApiError()
            .doOnSubscribe { progressDialog.postValue(true) }
            .doFinally { progressDialog.postValue(false) }
            .doOnSuccess {
                loginSuccessEvent.postValue(Event(Unit))
            }
            .bindLife()
    }
}