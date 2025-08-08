package com.cdsy.aichat.model.api.character

data class CharacterType(
    val id: String,
    val category_code: String,
    val sort: String,
    val language_code: String,
    val name: String,
)

data class CharacterTypeListModel(
    val list: List<CharacterType>
)