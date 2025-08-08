package com.cdsy.aichat.model.api.character

data class Character(
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
    val galleries: List<Gallery>
) {
    //图片 资源  筛选galleries中 selected为True的 > gallery_type为1的 > 第一个
    val poster
        get() = (galleries.firstOrNull { it.is_selected }
            ?: galleries.firstOrNull { it.gallery_type == 1 } ?: galleries[0]).thumbnail
}

data class Profile(
    val gender: String,
    val age: Int,
    val name: String,
    val summary: String
)

data class Tag(
    val tag_id: String,
    val tag_name: String
)

data class Stats(
    val message_count: Int,
    val follower_count: Int,
    val chat_user_count: Int
)

data class Heat(
    val recent_growth: Int,
    val recent_trending: Int,
    val average_growth: Int
)

const val FILE_TYPE_IMAGE = "image"
const val FILE_TYPE_VIDEO = "video"

data class Gallery(
    val id: String,
    val gallery_type: Int,
    val unlock_credit: Int,
    val unlock_intimacy: Int,
    val sort_index: Int,
    val is_unlocked: Boolean,
    val is_selected: Boolean,
    val assets: List<Asset>
) {
    val thumbnail: String get() = assets.firstOrNull { it.file_type == FILE_TYPE_IMAGE }?.url ?: ""
}

data class Asset(
    val file_type: String,
    val url: String,
    val file_size: Int,
    val mime_type: String,
    val metadata: Metadata
)

data class Metadata(
    val width: Int,
    val height: Int
)

data class CharacterListModel(
    val list: List<Character>
)