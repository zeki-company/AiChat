package com.cdsy.aichat.ui.club

import com.cdsy.aichat.R
import com.cdsy.aichat.databinding.ItemChatterTagBinding
import com.cdsy.aichat.model.api.character.Tag
import com.cdsy.aichat.ui.base.BaseRecyclerAdapter

class ChatterTagRecyclerAdapter(val onClick: (Tag) -> Unit) :
    BaseRecyclerAdapter<Tag, ItemChatterTagBinding>(
        R.layout.item_chatter_tag
    ) {
    override fun onCellClick(bean: Tag) {
        onClick(bean)
    }

    override fun bindData(binding: ItemChatterTagBinding, position: Int) {
        binding.tvTag.text = baseList[position].tag_name
    }
}