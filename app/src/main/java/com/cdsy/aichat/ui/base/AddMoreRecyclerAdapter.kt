package com.cdsy.aichat.ui.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

val VIEW_TYPE_ADD_MORE = 0

abstract class AddMoreRecyclerAdapter<Bean, Binding : ViewDataBinding>
constructor(
    layoutRes: Int,
    val addMoreLayoutRes: Int
) : BaseRecyclerAdapter<Bean, Binding>(
    layoutRes,
) {
    class AddMoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    abstract fun onAddMoreClick()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ADD_MORE)
            AddMoreViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    addMoreLayoutRes,
                    parent,
                    false
                )
            )
        else super.onCreateViewHolder(parent, viewType)
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + 1
    }

    override fun getItemViewType(position: Int): Int {
        //如果集合的长度为0时，使用emptyView的布局
        if (baseList.size == position) {
            return VIEW_TYPE_ADD_MORE
        }
        //如果有数据，则使用ITEM的布局
        return VIEW_TYPE_ITEM
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is AddMoreViewHolder -> {
                holder.itemView.setOnClickListener {
                    onAddMoreClick()
                }
            }

            else -> super.onBindViewHolder(holder, position)
        }
    }
}