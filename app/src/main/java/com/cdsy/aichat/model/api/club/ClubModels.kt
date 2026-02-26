package com.cdsy.aichat.model.api.club

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ClubListRequest(
    val lastId: String? = null,
    val pageSize: Int = 20
)

@JsonClass(generateAdapter = true)
data class ClubPageInfo(
    val haveMore: Int? = null,
    val lastId: String? = null,
    val topId: String? = null,
    val pageSize: Int? = null,
    val total: Int? = null
)

@JsonClass(generateAdapter = true)
data class ClubMedia(
    val id: String? = null,
    val coverImageUrl: String? = null,
    val url: String? = null,
    val title: String? = null,
    val description: String? = null,
    val likes: Long? = null,
    val updateAt: Long? = null
)

@JsonClass(generateAdapter = true)
data class ClubUser(
    val id: String? = null,
    val uuid: String? = null,
    val firstName: String? = null,
    val age: Int? = null,
    val region: String? = null,
    val vip: Int? = null,
    val label: Int? = null // 在线状态：0=隐藏，1/2/3 不同状态
)

@JsonClass(generateAdapter = true)
data class ClubItem(
    val media: ClubMedia? = null,
    val user: ClubUser? = null
) {
    val uid: String get() = user?.uuid.orEmpty()
    val displayNameAge: String
        get() {
            val name = user?.firstName.orEmpty()
            val age = user?.age ?: 0
            return if (age > 0) "$name, $age" else name
        }
    val coverUrl: String
        get() = media?.coverImageUrl
            ?: media?.url
            ?: ""

    val isVip: Boolean
        get() = user?.vip == 1

    val onlineLabel: Int
        get() = user?.label ?: 0
}

@JsonClass(generateAdapter = true)
data class ClubListResponse(
    val code: Int,
    val error: String? = null,
    val data: List<ClubItem>? = null,
    val page: ClubPageInfo? = null,
    val timestamp: Long? = null
)

/**
 * 新用户首次 1v1 通话优惠信息，对应旧项目 NewBonusCountDownEntity。
 */
@JsonClass(generateAdapter = true)
data class NewBonusInfo(
    val privateCallDiscount: Boolean = false,
    val privateCallDiscountExpireTime: Long? = null,
    val privateCallDiscountCoin: Long? = null,
    val privateCallDiscountPercent: String? = null,
    val coin: Long? = null
)

@JsonClass(generateAdapter = true)
data class NewBonusInfoResponse(
    val code: Int,
    val error: String? = null,
    val data: NewBonusInfo? = null,
    val timestamp: Long? = null
)

