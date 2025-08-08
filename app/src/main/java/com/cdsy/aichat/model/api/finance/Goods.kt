package com.cdsy.aichat.model.api.finance

import com.cdsy.aichat.ui.base.IChoose

data class Goods(
    val id: String,
    val product_type: String,
    val sort_index: Int,
    val extra_info: ExtraInfo,
    val create_time: Long,
    val sku: Sku,
) : IChoose {
    override var isSelected: Boolean = false
}

data class ExtraInfo(
    val id: String?
)

data class Sku(
    val iap_product_id: String,
    val credit: Int,
    val price: Float,
    val currency: String
)

data class GoodsListResultModel(
    val list: List<Goods>
)