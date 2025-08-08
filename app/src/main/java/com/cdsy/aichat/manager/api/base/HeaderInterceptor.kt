package com.cdsy.aichat.manager.api.base

import com.cdsy.aichat.BuildConfig
import com.cdsy.aichat.manager.sharedpref.SharedPrefModel
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor {

    companion object {
        // 自定义header标记，用于跳过Authorization和X-Device-Id
        const val SKIP_AUTH_HEADERS = "X-Skip-Auth-Headers"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()
        
        // 添加基础header
        requestBuilder
            .header("Content-Type", "application/json")
            .header("X-App-Id", BuildConfig.X_APP_ID)
            .header("X-App-Version", BuildConfig.VERSION_NAME)
        
        // 检查是否需要跳过认证相关header
        val skipAuthHeaders = originalRequest.header(SKIP_AUTH_HEADERS) == "true"
        
        if (!skipAuthHeaders) {
            // 只有在不跳过时才添加认证相关header
            requestBuilder
                .header("Authorization", "Bearer ${
                    SharedPrefModel.
                    getUserModel().token}" +
                        "")
                .header("X-Device-Id", SharedPrefModel.xDeviceId)
        }
        
        // 移除临时标记header，避免发送到服务器
        requestBuilder.removeHeader(SKIP_AUTH_HEADERS)
        
        return chain.proceed(requestBuilder.build())
    }

}