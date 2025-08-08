package com.cdsy.aichat.ui.login

import android.app.Application
import android.content.Intent
import com.cdsy.aichat.MainApplication
import com.cdsy.aichat.manager.api.AppService
import com.cdsy.aichat.manager.api.DeviceService
import com.cdsy.aichat.manager.api.GoogleSignInService
import com.cdsy.aichat.manager.api.UserService
import com.cdsy.aichat.manager.sharedpref.SharedPrefModel
import com.cdsy.aichat.model.api.ResultModel
import com.cdsy.aichat.model.api.app.STATUS_OFF
import com.cdsy.aichat.model.api.device.DeviceRequestModel
import com.cdsy.aichat.model.api.login.GoogleSignInRequestModel
import com.cdsy.aichat.ui.base.BaseViewModel
import com.cdsy.aichat.ui.base.Event
import com.cdsy.aichat.ui.base.LiveEvent
import com.cdsy.aichat.util.Constants
import com.cdsy.aichat.util.switchThread
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import io.reactivex.Single
import org.kodein.di.generic.instance

class LoginViewModel(application: Application) : BaseViewModel(application) {
    private val deviceService by instance<DeviceService>()
    private val appService by instance<AppService>()
    private val userService by instance<UserService>()
    private val googleSignInService = GoogleSignInService(application)

    val showLoginEvent = LiveEvent<Event<Boolean>>()
    val googleSignInIntentEvent = LiveEvent<Event<Intent>>()
    val googleSignInSuccessEvent = LiveEvent<Event<Boolean>>()
    val googleSignInErrorEvent = LiveEvent<Event<String>>()

    fun initDevice(deviceInfo: DeviceRequestModel) {
        deviceService.initDevice(deviceInfo)
            .flatMap {
                SharedPrefModel.xDeviceId = it.data.device_id
                SharedPrefModel.appEnvCode = it.data.app_env_code
                if (SharedPrefModel.appConfigVersionCode.isEmpty() || SharedPrefModel.appConfigVersionCode != it.data.app_config_version_code) {
                    SharedPrefModel.appConfigVersionCode = it.data.app_config_version_code
                    appService.getAppConfig()
                } else Single.just(ResultModel(success = true, data = SharedPrefModel.appConfig))
            }.flatMap {
                SharedPrefModel.appConfig = it.data
                appService.checkForUpdate()
            }
            .switchThread()
            .catchApiError()
            .doOnSuccess {
                if (it.data.current_version.status != STATUS_OFF) {
                    if (SharedPrefModel.appEnvCode != Constants.DEVELOPMENT_ENV_CODE) {
                        showLoginEvent.postValue(Event(true))
                    }
                }
            }

            .bindLife()
    }

    fun startGoogleSignIn() {
        progressDialog.postValue(true)
        val signInIntent = googleSignInService.getSignInIntent()
        googleSignInIntentEvent.postValue(Event(signInIntent))
    }

    //从Google获取到的登录结果
    fun handleGoogleSignInResult(data: Intent?) {
        googleSignInService.handleSignInResult(data)
            .flatMap { account: GoogleSignInAccount ->
                SharedPrefModel.userId = account.id ?: ""
                MainApplication.nowUserId = account.id ?: ""
                SharedPrefModel.setUserModel {
                    portrait = account.photoUrl.toString()
                    name = account.displayName ?: ""
                }
                userService.signInByGoogle(
                    GoogleSignInRequestModel(
                        google_id_token = account.idToken ?: "",
                        google_user_id = account.id ?: ""
                    )
                )
            }
            .switchThread()
            .doOnSuccess {
                SharedPrefModel.setUserModel { token = it.data.token }
                googleSignInSuccessEvent.postValue(Event(true))
            }
            .doOnError { exception: Throwable ->
                googleSignInErrorEvent.postValue(Event(exception.message ?: "Google登录失败"))
            }
            .doFinally { progressDialog.postValue(false) }
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
}