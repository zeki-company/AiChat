package com.cdsy.aichat.ui.base

import androidx.databinding.ViewDataBinding

/**
 * @Author: ZEKI https://github.com/ZYF99
 * @Date: 2021/6/16 3:41 下午
 */
abstract class SingleChooseRecyclerAdapter<T : IChoose, B : ViewDataBinding>(
    layoutRes: Int,
    private val onItemClick: (T) -> Unit
) : BaseRecyclerAdapter<T, B>(
    layoutRes
) {
    override fun bindData(binding: B, position: Int) {
        val item = baseList[position]
        binding.root.isSelected = item.isSelected
    }

    override fun onCellClick(bean: T) {
        baseList.forEach {
            it.isSelected = it == bean
        }
        notifyDataSetChanged()
        onItemClick(bean)
    }

    fun getSelectedItem() = baseList.firstOrNull { it.isSelected }
    fun clearSelects() {
        baseList.forEach { it.isSelected = false }
        notifyDataSetChanged()
    }
}