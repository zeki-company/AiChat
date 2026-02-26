package com.cdsy.aichat.manager.api.base

import com.cdsy.aichat.BuildConfig
import com.cdsy.aichat.manager.sharedpref.SharedPrefModel
import com.cdsy.aichat.util.DeviceUtil
import okhttp3.Interceptor
import okhttp3.Response
import java.net.URLEncoder
import java.util.Locale

class HeaderInterceptor : Interceptor {

    companion object {
        // 自定义header标记，用于跳过Authorization和X-Device-Id
        const val SKIP_AUTH_HEADERS = "X-Skip-Auth-Headers"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url
        val requestBuilder = originalRequest.newBuilder()

        // ===== 通用基础 Header（旧项目的公共头部参数） =====
        // 旧项目: lang = "<language-country>;<timezoneId>"
        val locale = Locale.getDefault()
        val langCode = "${locale.language}-${locale.country}"
        val lang = "$langCode;${DeviceUtil.getTimezone()}"
        val buildCode = BuildConfig.VERSION_CODE.toString()
        val buildName = BuildConfig.VERSION_NAME
        val channel = "2" // 旧项目固定为 2
        val appId = BuildConfig.APP_ID

        // 请求体 JSON（用于 requestParams 和签名）。这里只在 body 可重复读取且为 JSON 时尝试获取。
        var requestJson = ""
        originalRequest.body?.let { body ->
            try {
                val buffer = okio.Buffer()
                body.writeTo(buffer)
                requestJson = buffer.readUtf8()
            } catch (_: Exception) {
            }
        }

        // 时间戳 & 签名
        val timestamp = System.currentTimeMillis().toString()
        val urlPath = originalUrl.encodedPath // 例如 /v1/login
        val signString = buildString {
            append("appId=").append(appId)
            append("&secret=").append(BuildConfig.APP_SECRET)
            append("&channel=").append(channel)
            append("&build=").append(buildCode)
            if (requestJson.isNotEmpty()) {
                append("&").append(requestJson)
            }
            append("&url=").append(
                if (urlPath.startsWith("/")) urlPath.substring(1) else urlPath
            )
            append("&timestamp=").append(timestamp)
        }
        val sign = md5(signString)

        // 旧项目 header: requestParams = 请求体 JSON 的 UTF-8 URL 编码
        val requestParams =
            if (requestJson.isNotEmpty()) URLEncoder.encode(requestJson, "UTF-8") else ""

        requestBuilder
            .header("Content-Type", "application/json; charset=utf-8")
            .header("build", buildCode)
            .header("appId", appId)
            .header("channel", channel)
            .header("requestParams", requestParams)
            .header("lang", lang)
            .header("version", buildName)
            .header("sign", sign)
            .header("timestamp", timestamp)



        // 检查是否需要跳过认证相关header
        val skipAuthHeaders = originalRequest.header(SKIP_AUTH_HEADERS) == "true"

        if (!skipAuthHeaders) {
            // 只有在不跳过时才添加认证相关header
            val token = SharedPrefModel.getUserModel().token
            requestBuilder
                // 旧项目 header: token
                .header("token", token)
        }

        // 移除临时标记header，避免发送到服务器
        requestBuilder.removeHeader(SKIP_AUTH_HEADERS)

        return chain.proceed(requestBuilder.build())
    }

    private fun md5(text: String): String {
        return try {
            val md = java.security.MessageDigest.getInstance("MD5")
            val bytes = md.digest(text.toByteArray())
            bytes.joinToString("") { "%02x".format(it) }
        } catch (e: Exception) {
            ""
        }
    }

}