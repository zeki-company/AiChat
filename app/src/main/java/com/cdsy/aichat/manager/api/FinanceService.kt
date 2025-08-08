package com.cdsy.aichat.manager.api

import com.cdsy.aichat.model.api.ResultModel
import com.cdsy.aichat.model.api.finance.GoodsListResultModel
import com.cdsy.aichat.model.api.finance.ReceiptResultModel
import com.cdsy.aichat.model.api.finance.SubmitReceiptRequestModel
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/*************************************************金融相关******************************************/
interface FinanceService {

    //获取商品列表
    @GET("product/list")
    fun fetchGoodsList(): Single<ResultModel<GoodsListResultModel>>

    //提交小票（创建购买）
    @POST("purchase")
    fun submitReceipt(
        @Body submitReceiptRequestModel: SubmitReceiptRequestModel
    ): Single<ResultModel<ReceiptResultModel>>

}
