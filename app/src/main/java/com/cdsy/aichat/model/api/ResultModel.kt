package com.cdsy.aichat.model.api

import com.squareup.moshi.JsonClass

/**
 * 统一的服务端返回结构：
 * {
 *   "code": 200,
 *   "error": "xxx",
 *   "data": {...},
 *   "page": null,
 *   "timestamp": 123456789
 * }
 */
@JsonClass(generateAdapter = true)
data class ResultModel<T>(
    val code: Int,
    val error: String? = null,
    val data: T,
    val page: Any? = null,
    val timestamp: Long? = null
)