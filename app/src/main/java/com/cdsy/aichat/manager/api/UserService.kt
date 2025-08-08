package com.cdsy.aichat.manager.api

import com.cdsy.aichat.model.api.ResultModel
import com.cdsy.aichat.model.api.login.GoogleSignInRequestModel
import com.cdsy.aichat.model.api.login.TokenResult
import com.cdsy.aichat.model.api.user.Balance
import com.cdsy.aichat.model.api.user.UserInfo
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/*************************************************用户相关******************************************/
interface UserService {

    //Google登录
    @POST("auth/google/login")
    fun signInByGoogle(
        @Body googleSignInRequestModel: GoogleSignInRequestModel
    ): Single<ResultModel<TokenResult>>

    //登出
    @POST("auth/logout")
    fun signOut(): Single<ResultModel<Any>>

    //查询用户信息
    @GET("user")
    fun fetchUserInfo(): Single<ResultModel<UserInfo>>

    //查询用户余额
    @GET("user/balance")
    fun fetchUserBalance(): Single<ResultModel<Balance>>

}
