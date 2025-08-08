package com.cdsy.aichat.ui.me

import com.cdsy.aichat.R
import com.cdsy.aichat.databinding.ItemTopUpBinding
import com.cdsy.aichat.model.api.finance.Goods
import com.cdsy.aichat.ui.base.SingleChooseRecyclerAdapter

class TopUpRecyclerAdapter(onItemClick: (Goods) -> Unit) :
    SingleChooseRecyclerAdapter<Goods, ItemTopUpBinding>(
        R.layout.item_top_up,
        onItemClick
    ) {
    override fun bindData(binding: ItemTopUpBinding, position: Int) {
        super.bindData(binding, position)
        val item = baseList[position]
        binding.item = item
        binding.root.isSelected = item.isSelected
        binding.tvToken.text = "${item.sku.credit} tokens"
        binding.tvMoney.text =
            "${binding.root.context.getString(R.string.currency_flag)}${item.sku.price}"
    }
}