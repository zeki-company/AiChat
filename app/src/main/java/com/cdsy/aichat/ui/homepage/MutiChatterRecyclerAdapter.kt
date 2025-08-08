package com.cdsy.aichat.ui.homepage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cdsy.aichat.R
import com.cdsy.aichat.databinding.ItemAdsBannerBinding
import com.cdsy.aichat.databinding.ItemChatterBannerBinding
import com.cdsy.aichat.databinding.ItemChatterNormalBinding
import com.cdsy.aichat.model.api.character.Character
import com.cdsy.aichat.ui.base.BaseRecyclerAdapter
import com.cdsy.aichat.ui.splash.BannerAdapter

val TYPE_CHATTER_NORMAL = 1
val TYPE_CHATTER_BANNER = 2
val TYPE_ADS_BANNER = 3
val mockUrl =
    "https://iknow-pic.cdn.bcebos.com/86d6277f9e2f07086706a89dfb24b899a901f228?for=bg"

class MutiChatterRecyclerAdapter(val lifecycle: Lifecycle, val onClick: (Character) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var characterList: List<Character> = emptyList()

    class BaseSimpleViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_CHATTER_NORMAL -> BaseSimpleViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_chatter_normal, parent, false)
            )

            TYPE_CHATTER_BANNER -> BaseSimpleViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_chatter_banner, parent, false)
            )

            TYPE_ADS_BANNER -> BaseSimpleViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_ads_banner, parent, false)
            )

            else -> BaseSimpleViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_chatter_normal, parent, false)
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = characterList[position]
        return when {
            item.galleries.size > 1 -> TYPE_CHATTER_BANNER
            else -> TYPE_CHATTER_NORMAL
        }
    }

    override fun getItemCount() = characterList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as BaseSimpleViewHolder
        var binding: ViewDataBinding? = DataBindingUtil.getBinding(holder.itemView)
        if (binding == null) {
            binding = DataBindingUtil.bind(holder.itemView)
        }

        when (getItemViewType(position)) {
            TYPE_CHATTER_NORMAL -> binding as ItemChatterNormalBinding
            TYPE_CHATTER_BANNER -> binding as ItemChatterBannerBinding
            TYPE_ADS_BANNER -> binding as ItemAdsBannerBinding
        }
        binding!!.root.setOnClickListener {
            onClick(characterList[position])
        }
        binding.let { bindData(it, position) }
    }


    private fun bindData(binding: ViewDataBinding, position: Int) {
        when (binding) {
            is ItemChatterNormalBinding -> {
                binding.character = characterList[position]
                binding.rvTag.apply {
                    layoutManager = LinearLayoutManager(context).apply {
                        orientation = LinearLayoutManager.HORIZONTAL
                    }
                    adapter = ChatterTagRecyclerAdapter {}.apply {
                        replaceData(characterList[position].tags)
                    }
                }
            }

            is ItemChatterBannerBinding -> {
                binding.character = characterList[position]
                binding.bannerVp
                    .registerLifecycleObserver(lifecycle)
                    .setAdapter(BannerAdapter(true))
                    .setIndicatorSliderWidth(16, 48)
                    .setIndicatorHeight(16)
                    .setIndicatorSliderGap(12)
                    .create()
                binding.bannerVp.refreshData(
                    listOf(mockUrl, mockUrl, mockUrl)
                )
            }

            is ItemAdsBannerBinding -> {
                binding.character = characterList[position]
                binding.bannerVp
                    .registerLifecycleObserver(lifecycle)
                    .setAdapter(BannerAdapter(false))
                    .setIndicatorSliderWidth(16, 48)
                    .setIndicatorHeight(16)
                    .setIndicatorSliderGap(12)
                    .create()
                binding.bannerVp.refreshData(
                    characterList[position].galleries.flatMap { it.assets }.map { it.url }
                )
            }
        }
    }

    fun replaceData(newList: List<Character>) {
        characterList = newList
        notifyDataSetChanged()
    }


}
