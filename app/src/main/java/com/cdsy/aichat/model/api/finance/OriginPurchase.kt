package com.cdsy.aichat.model.api.finance

data class OriginPurchase(
    val packageName:String,
    val acknowledged:Boolean,
    val orderId:String,
    val productId:String,
    val developerPayload:String,
    val purchaseTime:Long,
    val purchaseState:Long,
    val purchaseToken:String,
)