package com.cdsy.aichat.model.api.user

data class UserInfo(
    val id: String,
    val env_type: Int,
    val create_time: Long,
    val balance: Balance,
    val profile: Profile,
    val preference: Preference,
    val session: Session,
    val auths: List<Auth>
)

data class Balance(
    val credit: Int,
    val total_earned: Int,
    val total_spent: Int,
    val total_rewarded: Int,
    val total_recharged: Int
)

data class Profile(
    val update_time: Long
)

data class Preference(
    val id: String?
)

data class Session(
    val auth_provider: String,
    val last_login_time: Long
)

data class Auth(
    val provider: String,
    val is_verified: Boolean,
    val create_time: Long
)