package com.cdsy.aichat.manager.api.base

import com.cdsy.aichat.util.fromJson
import com.squareup.moshi.JsonClass
import okhttp3.Interceptor
import okhttp3.Response

class NetErrorInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        if (response.code in 400..503) {
            val bodyString = response.body?.string()
            val e = globalMoshi.fromJson<ErrorResultModel>(bodyString)
            if (e != null) {
                // 和正常结构一致：code / error / data / page / timestamp
                throw ServerError(e.code, e.error ?: "API Error")
            } else {
                throw ServerError(9999, "API UnKnown Error")
            }
        }
        return response
    }

}

data class ServerError(val code: Int, override val message: String) : RuntimeException()

/**
 * 错误时服务端返回结构（和 ResultModel 同结构，只是我们只关心 code / error）
 */
@JsonClass(generateAdapter = true)
data class ErrorResultModel(
    val code: Int,
    val error: String? = null,
    val data: Any? = null,
    val page: Any? = null,
    val timestamp: Long? = null
)