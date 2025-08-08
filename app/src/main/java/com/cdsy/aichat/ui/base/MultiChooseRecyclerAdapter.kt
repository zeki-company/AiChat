package com.cdsy.aichat.ui.base

import androidx.databinding.ViewDataBinding

/**
 * @Author: ZEKI https://github.com/ZYF99
 * @Date: 2021/6/16 3:41 下午
 */
abstract class MultiChooseRecyclerAdapter<T : IChoose, B : ViewDataBinding>(
    layoutRes: Int,
    private val onCellClick: (T) -> Unit
) : BaseRecyclerAdapter<T, B>(
    layoutRes,
    emptyList()
) {
    override fun bindData(binding: B, position: Int) {
        val item = baseList[position]
        binding.root.isSelected = item.isSelected
        binding.root.setOnClickListener {
            item.isSelected = !item.isSelected
            notifyDataSetChanged()
            onCellClick(item)
        }
    }

    fun getSelectedItem() = baseList.filter { it.isSelected }

}