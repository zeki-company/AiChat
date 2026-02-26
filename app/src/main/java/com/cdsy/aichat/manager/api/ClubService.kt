package com.cdsy.aichat.manager.api

import com.cdsy.aichat.model.api.club.ClubListRequest
import com.cdsy.aichat.model.api.club.ClubListResponse
import com.cdsy.aichat.model.api.club.NewBonusInfoResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * 对应旧项目 ApiService 中的：
 * - v1/club/search
 * - v1/video/call/coin
 */
interface ClubService {

    @POST("v1/club/search")
    fun getClubList(
        @Body body: ClubListRequest
    ): Single<ClubListResponse>

    @POST("v1/video/call/coin")
    fun getNewBonusInfo(): Single<NewBonusInfoResponse>
}

