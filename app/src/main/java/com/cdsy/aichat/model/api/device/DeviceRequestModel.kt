package com.cdsy.aichat.model.api.device

data class DeviceRequestModel(
    val unique_id: String,
    val platform: String,
    val app_id: String,
    val app_version: String,
    val device_name: String? = null,
    val device_model: String? = null,
    val os_name: String? = null,
    val os_version: String? = null,
    val timezone: String? = null,
    val language: String? = null,
    val is_rooted: String? = null,
    val screen_width: Int? = null,
    val screen_height: Int? = null,
    val pixel_ratio: Double? = null,
    val network: NetworkInfo? = null
)


data class NetworkInfo(
    val network_type: String? = null,
    val mcc: String? = null,
    val mnc: String? = null,
    val sim_country_code: String? = null,
    val sim_operator: String? = null,
    val is_vpn_connected: Boolean = false,
    val is_proxy_enabled: String? = null
)

