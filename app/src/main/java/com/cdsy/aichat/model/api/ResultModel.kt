package com.cdsy.aichat.model.api

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResultModel<T>(
    val success: Boolean,
    val data: T
)