package com.cdsy.aichat.model.api.character

data class AiMode(
    val id: String,
    val credit: Int,
    val is_membership: Boolean?,
    val is_enabled: Boolean,
    val is_public: Boolean,
    val language_code: String,
    val name: String,
    val description: String,
    val is_selected: Boolean
)

//当前版本没有选择模型功能，直接从公共模型库取第一个
fun List<AiMode>.getCredit(): Int {
    return this.last().credit
}

data class UIModeListModel(
    val list: List<AiMode>
)