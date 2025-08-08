package com.cdsy.aichat.manager.api

import com.cdsy.aichat.model.api.ResultModel
import com.cdsy.aichat.model.api.app.AppConfigModel
import com.cdsy.aichat.model.api.app.VersionModel
import io.reactivex.Single
import retrofit2.http.GET

/*************************************************APP配置相关******************************************/
interface AppService {
    @GET("app/config")
    fun getAppConfig(): Single<ResultModel<AppConfigModel>>

    @GET("app/update")
    fun checkForUpdate(): Single<ResultModel<VersionModel>>


}
