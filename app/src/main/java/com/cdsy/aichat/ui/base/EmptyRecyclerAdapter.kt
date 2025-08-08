package com.cdsy.aichat.ui.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.cdsy.aichat.R

/**
 * @Author: ZEKI https://github.com/ZYF99
 * @Date: 2021/6/10 9:52 上午
 */
/**
 * viewType--分别为item以及空view
 */


abstract class EmptyRecyclerAdapter<Bean, Binding : ViewDataBinding>
constructor(
    layoutRes: Int,
    val emptyLayoutRes: Int
) : BaseRecyclerAdapter<Bean, Binding>(
    layoutRes,
) {
    class EmptyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_EMPTY)
            EmptyViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    emptyLayoutRes,
                    parent,
                    false
                )
            )
        else super.onCreateViewHolder(parent, viewType)
    }

    override fun getItemCount(): Int {
        //同时这里也需要添加判断，如果mData.size()为0的话，只引入一个布局，就是emptyView
        // 那么，这个recyclerView的itemCount为1
        if (baseList.isEmpty()) {
            return 1
        }
        //如果不为0，按正常的流程跑
        return super.getItemCount()
    }

    override fun getItemViewType(position: Int): Int {
        //如果集合的长度为0时，使用emptyView的布局
        if (baseList.isEmpty()) {
            return VIEW_TYPE_EMPTY
        }
        //如果有数据，则使用ITEM的布局
        return VIEW_TYPE_ITEM
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is EmptyViewHolder -> {
            }
            else -> super.onBindViewHolder(holder, position)
        }
    }
}
