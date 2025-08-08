package com.cdsy.aichat.ui.message

data class Message(
    val title: String,
    val content: String,
    val characterId: String,
    val portrait: String,
    val unReadCount: Int,
    val time: String,
    val isSystem: Boolean
) {
    val haveUnRead get() = unReadCount > 0 && !isSystem
}