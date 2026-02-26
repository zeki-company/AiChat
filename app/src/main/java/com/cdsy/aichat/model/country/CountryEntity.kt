package com.cdsy.aichat.model.country

/**
 * 对应旧项目的 CountryEntity，简化为纯数据类。
 */
data class CountryEntity(
    val iso: String,
    val phoneCode: String,
    val name: String
)

