package com.cdsy.aichat.manager.api.base

import com.cdsy.aichat.util.fromJson
import com.squareup.moshi.JsonClass
import okhttp3.Interceptor
import okhttp3.Response

class NetErrorInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        if (response.code in 400..503) {
            val e = globalMoshi.fromJson<ErrorResultModel>(response.body?.string())
            if (e != null)
                throw e.error
            else throw ServerError(9999, "API UnKnown Error")
        }
        return response
    }

}

data class ServerError(val code: Int, override val message: String) : RuntimeException()

@JsonClass(generateAdapter = true)
data class ErrorResultModel(val success: Boolean, val error: ServerError)