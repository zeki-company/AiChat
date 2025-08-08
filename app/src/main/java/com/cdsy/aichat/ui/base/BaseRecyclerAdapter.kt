package com.cdsy.aichat.ui.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerAdapter<Bean, Binding : ViewDataBinding>(private val layoutRes: Int) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var baseList: List<Bean> = emptyList()

    constructor(layoutRes: Int, initList: List<Bean>) : this(layoutRes) {
        baseList = initList
    }

    class BaseSimpleViewHolder<Binding : ViewDataBinding>(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        val binding: Binding? by lazy {
            DataBindingUtil.bind(itemView)
        }
    }

    abstract fun onCellClick(bean: Bean)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return BaseSimpleViewHolder<Binding>(
            LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
        )
    }

    override fun getItemCount() = baseList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as BaseSimpleViewHolder<Binding>
        holder.binding!!.root.setOnClickListener {
            onCellClick(baseList[position])
        }
        bindData(holder.binding!!, position)
    }

    abstract fun bindData(binding: Binding, position: Int)

    open fun replaceData(newList: List<Bean>) {
        if (baseList.isEmpty()) {
            baseList = newList
            notifyDataSetChanged()
        } else {
            if (newList.isNotEmpty()) {
                val diffResult =
                    DiffUtil.calculateDiff(
                        SingleBeanDiffCallBack(
                            baseList,
                            newList
                        ), true
                    )
                baseList = newList
                diffResult.dispatchUpdatesTo(this)
            } else {
                baseList = newList
                notifyDataSetChanged()
            }
        }
    }

    open fun insertData(newData: Bean) {
        baseList = baseList.toMutableList().apply { add(newData) }
        notifyItemInserted(baseList.size - 1)
    }

    open fun removeData(oldData: Bean) {
        replaceData(
            baseList.toMutableList()
                .apply { remove(oldData) })
    }
}


class SingleBeanDiffCallBack<Bean>(
    val oldDatas: List<Bean>,
    val newDatas: List<Bean>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldData = oldDatas[oldItemPosition]
        val newData = newDatas[newItemPosition]
        return oldData == newData
    }

    override fun getOldListSize(): Int {
        return oldDatas.size
    }

    override fun getNewListSize(): Int {
        return newDatas.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldData = oldDatas[oldItemPosition]
        val newData = newDatas[newItemPosition]
        return oldData == newData
    }
}

