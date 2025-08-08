package com.cdsy.aichat.model.api.finance

//向服务器提交小票的request 类
data class SubmitReceiptRequestModel(
    val product_id: String, //商品id
    val receipt_data: String //小票数据
)