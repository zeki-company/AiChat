package com.cdsy.aichat.model.api.login

import com.squareup.moshi.JsonClass

/**
 * 兼容旧项目登录 / 验证码相关接口的数据模型（只保留本项目需要的字段）
 */

@JsonClass(generateAdapter = true)
data class LoginRequest(
    val afSource: String? = null, // AppsFlyer 渠道：如 "organic"、"facebook"
    val afType: Int? = null,      // 1:广告用户，0:自然用户
    val deviceInfo: DeviceInfo? = null,
    val source: String,
    val token: String
)

@JsonClass(generateAdapter = true)
data class DeviceInfo(
    val fcmToken: String?,          // 推送 token
    val hardware: String?,          // 硬件型号，如 "alioth/M2012K11AC"
    val imei: String?,              // 设备唯一标识
    val os: String?,                // 如 "Android 15"
    val packageBuild: String?,      // 构建号，如 "180"
    val packageBundleId: String?,   // 包名，如 "com.hiyak.chat"
    val packageVersion: String?,    // 应用版本号，如 "2.4.1"
    val platform: Int = 2,          // 2: Android
    val language: String?,          // 语言，如 "zh-TW"
)

@JsonClass(generateAdapter = true)
data class LoginResult(
    val token: TokenInfo?,
    val user: UserSummary?
)

@JsonClass(generateAdapter = true)
data class TokenInfo(
    val token: String?,
    val refToken: String?
)

@JsonClass(generateAdapter = true)
data class UserSummary(
    val id: Long?,
    val uuid: String?,
    val firstName: String?,
    val gender: Int?,
    val age: Int?,
)

@JsonClass(generateAdapter = true)
data class CaptchaSendEmailRequest(
    val email: String,
    val type: Int? = 0
)

@JsonClass(generateAdapter = true)
data class CaptchaSendEmailResult(
    val action: Int,
    val code: String?,
    val key: String?
)

@JsonClass(generateAdapter = true)
data class CaptchaSendPhoneRequest(
    val phone: String,
    val callCode: String,
    val type: Int? = 0
)


