package com.cdsy.aichat.model.api.login

data class GoogleSignInRequestModel(
    val google_id_token: String,
    val google_user_id: String
)