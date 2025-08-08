package com.cdsy.aichat.model.api.app

data class AppConfigModel(
    val version_code: String = "",
    val finance_config: FinanceConfig = FinanceConfig(
        ad_reward_enabled = false,
        ad_reward_credit = 0,
        ad_reward_max_count = 0,
        checkin_reward_enabled = false
    ),
    val character_config: CharacterConfig = CharacterConfig(
        credit_intimacy_rate = 0f
    ),
    val client_config: ClientConfig = ClientConfig(),
)

data class FinanceConfig(
    val ad_reward_enabled: Boolean,
    val ad_reward_credit: Int,
    val ad_reward_max_count: Int,
    val checkin_reward_enabled: Boolean,
)

data class CharacterConfig(
    val credit_intimacy_rate: Float
)

data class ClientConfig(
    val terms_of_service_url: String = "",
    val privacy_policy_url: String = "",
    val discord_url: String = ""
)