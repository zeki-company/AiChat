package com.cdsy.aichat.manager.api

import com.cdsy.aichat.manager.api.base.ApiHeaders
import com.cdsy.aichat.model.api.ResultModel
import com.cdsy.aichat.model.api.device.DeviceRequestModel
import com.cdsy.aichat.model.api.device.DeviceResultModel
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

/*************************************************设备API******************************************/
interface DeviceService {
    /**
     * 初始化设备：需要跳过认证header
     * 这个请求将不会包含Authorization和X-Device-Id header
     */
    @POST("device/launch")
    fun initDevice(
        @Body deviceInfo: DeviceRequestModel,
        @Header(ApiHeaders.SKIP_AUTH_HEADERS) skipAuth: String = ApiHeaders.skipAuthHeaders()
    ): Single<ResultModel<DeviceResultModel>>

}