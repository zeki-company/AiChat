package com.cdsy.aichat.ui.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.cdsy.aichat.R
import java.lang.RuntimeException

/**
 * @Author: ZEKI https://github.com/ZYF99
 * @Date: 2021/6/10 9:52 上午
 */
/**
 * viewType--分别为 item view header tail
 */
const val VIEW_TYPE_EMPTY = 0
const val VIEW_TYPE_ITEM = 1
const val VIEW_TYPE_HEADER = 2
const val VIEW_TYPE_TAIL = 3

abstract class HeaderRecyclerAdapter<Bean, Binding : ViewDataBinding, HeaderBinding : ViewDataBinding, TailBinding : ViewDataBinding>(
    itemLayoutRes: Int,
    private val headerRes: Int? = null,
) : BaseRecyclerAdapter<Bean, Binding>(
    itemLayoutRes,
) {

    private var headerBinding: HeaderBinding? = null
    private var tailBinding: TailBinding? = null

    class EmptyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    class TailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_EMPTY -> EmptyViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.layout_empty,
                    parent,
                    false
                )
            )

            VIEW_TYPE_HEADER -> {
                if (headerBinding == null) {
                    headerBinding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        headerRes!!,
                        parent,
                        false
                    )
                } else throw RuntimeException("No Header Exception")
                HeaderViewHolder(headerBinding!!.root)
            }

            VIEW_TYPE_TAIL -> {
                if (tailBinding == null) {
                    tailBinding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.item_no_more,
                        parent,
                        false
                    )
                } else throw RuntimeException("No Tail Exception")
                TailViewHolder(tailBinding!!.root)

            }

            else -> super.onCreateViewHolder(parent, viewType)
        }
    }

    override fun getItemCount(): Int {
        //同时这里也需要添加判断，如果mData.size()为0的话，只引入一个布局，就是emptyView
        // 那么，这个recyclerView的itemCount为1
        return if (baseList.isEmpty()) {
            if (headerRes == null) 1
            else 2
        } else {
            //如果不为0，按正常的流程跑
            if (headerRes == null) super.getItemCount() + 1 //元素个数 + 尾巴
            else super.getItemCount() + 2 //元素个数 + 头 + 尾巴
        }
    }

    override fun getItemViewType(position: Int): Int {
        //如果集合的长度为0时，使用emptyView的布局
        return if (baseList.isEmpty()) {
            if (headerRes == null) VIEW_TYPE_EMPTY
            else {
                if (position == 0) VIEW_TYPE_HEADER
                else VIEW_TYPE_EMPTY
            }
        } else {
            //如果有数据
            if (headerRes == null) {
                if (position == baseList.size) VIEW_TYPE_TAIL else VIEW_TYPE_ITEM
            } else {
                when (position) {
                    0 -> VIEW_TYPE_HEADER
                    baseList.size + 1 -> VIEW_TYPE_TAIL
                    else -> VIEW_TYPE_ITEM
                }
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is EmptyViewHolder -> {

            }

            is HeaderViewHolder -> {
                headerBinding?.let { bindHeader(it) }
            }

            is TailViewHolder -> {
                tailBinding?.let { bindTail(it) }
            }

            else -> {
                if (headerBinding == null) super.onBindViewHolder(holder, position)
                else super.onBindViewHolder(holder, position - 1)
            }
        }
    }

    abstract fun bindHeader(headerBinding: HeaderBinding)
    abstract fun bindTail(tailBinding: TailBinding)
}
