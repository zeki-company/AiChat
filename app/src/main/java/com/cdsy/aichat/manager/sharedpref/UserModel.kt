package com.cdsy.aichat.manager.sharedpref

import com.cdsy.aichat.model.api.finance.SubmitReceiptRequestModel

class UserModel {
    var userId: String = ""
    var name: String = ""
    var portrait: String = ""
    var token: String = ""

    var notificationOn: Boolean = false

    //Google内购成功 但是上传服务器失败的小票列表
    //需要在定时器中反复检查是否存在小票，如果有就向服务器提交
    var unSubmitReceiptList = mutableListOf<SubmitReceiptRequestModel>()
}
