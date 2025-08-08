package com.cdsy.aichat.manager.api.base

/**
 * API Header工具类
 * 提供便捷的方法来添加各种header标记
 */
object ApiHeaders {

    /**
     * 跳过认证相关header（Authorization和X-Device-Id）
     * 使用方法：在API接口方法上添加 @Header("X-Skip-Auth-Headers", "true")
     */
    const val SKIP_AUTH_HEADERS = "X-Skip-Auth-Headers"

    /**
     * 获取跳过认证header的值
     * 可以直接在@Header注解中使用
     */
    fun skipAuthHeaders(): String = "true"

    /**
     * 获取跳过认证header的键值对
     * 可以在需要动态添加header时使用
     */
    fun getSkipAuthHeadersPair(): Pair<String, String> = SKIP_AUTH_HEADERS to "true"
} 