package com.cdsy.aichat.model.api.finance

//服务器返回的小票结果
data class ReceiptResultModel(
    val id: String,
    val purchase_status: Int,
    val product_id: String,
    val transaction_id: String,
    val failure_reason: Int,
    val verified_time: Long,
    val create_time: Long,
    val update_time: Long,
)