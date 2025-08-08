package com.cdsy.aichat.model.api.character

data class CharacterDetail(
    val id: String,
    val character_type: Int,
    val env_type: Int,
    val language_mode: String,
    val is_public: Boolean,
    val is_membership: Boolean?,
    val is_nsfw: Boolean,
    val is_deleted: Boolean,
    val create_time: Long,
    val is_followed: Boolean,
    val profile: Profile,
    val tags: List<Tag>,
    val stats: Stats,
    val heat: Heat,
    val avatars: List<Avatar>,
    val galleries: List<Gallery>,
    val user_stats: UserStats?,
    val user_preference: UserPreference?
) {
    //图片 资源  筛选galleries中 selected为True的 > gallery_type为1的 > 第一个
    val poster
        get() = (galleries.firstOrNull { it.is_selected }
            ?: galleries.firstOrNull { it.gallery_type == 1 } ?: galleries[0]).thumbnail
}


data class Avatar(
    val id: String,
    val avatar_type: Int,
    val unlock_credit: Double,
    val unlock_intimacy: Double,
    val sort_index: Int,
    val is_unlocked: Boolean,
    val is_selected: Boolean,
    val asset: Asset
)


data class UserStats(
    val message_count: Int,
    val message_credit_cost: Int,
    val gallery_credit_cost: Int,
    val avatar_credit_cost: Int,
    val intimacy_credit_cost: Int,
    val intimacy: Int
)

data class UserPreference(
    val selected_avatar_id: String,
    val selected_gallery_id: String,
    val selected_model_id: String,
    val custom_config: Map<String, String>
)