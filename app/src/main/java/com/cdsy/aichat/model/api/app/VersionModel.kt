package com.cdsy.aichat.model.api.app

const val STATUS_DRAFT = 1           //草稿
const val STATUS_WAIT_REVIEW = 2     //待审核
const val STATUS_WAIT_PASS = 3       //审核已通过
const val STATUS_BEING_OFF = 4       //即将下线
const val STATUS_OFF = 5             //已下线

data class VersionModel(
    val current_version: Version,
    val new_version: NewVersion? = null
)

data class Version(
    val status: Int
)

data class NewVersion(
    val app_version: String,
    val status: Int,
    val notes: String,
    val create_time: Long
)