package com.cdsy.aichat.manager.api

import com.cdsy.aichat.model.api.ResultModel
import com.cdsy.aichat.model.api.login.CaptchaSendEmailRequest
import com.cdsy.aichat.model.api.login.CaptchaSendEmailResult
import com.cdsy.aichat.model.api.login.CaptchaSendPhoneRequest
import com.cdsy.aichat.model.api.login.LoginRequest
import com.cdsy.aichat.model.api.login.LoginResult
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * 登录 / 注册 / 完善资料相关接口
 * 从旧项目抽取而来，按当前项目风格统一为 Single + ResultModel。
 */
interface LoginService {

    /**
     * 登录（Google / Facebook / Phone / Email 最终都走这里）
     * 对应旧项目 v1/login
     */
    @POST("v1/login")
    fun login(
        @Body body: LoginRequest
    ): Single<ResultModel<LoginResult>>

    /**
     * 发送邮箱验证码
     * 对应旧项目 v1/email/captcha/send
     */
    @POST("v1/email/captcha/send")
    fun sendEmailCaptcha(
        @Body body: CaptchaSendEmailRequest
    ): Single<ResultModel<CaptchaSendEmailResult>>

    /**
     * 发送手机验证码
     * 对应旧项目 v1/captcha/send
     */
    @POST("v1/captcha/send")
    fun sendPhoneCaptcha(
        @Body body: CaptchaSendPhoneRequest
    ): Single<ResultModel<CaptchaSendEmailResult>>
}

