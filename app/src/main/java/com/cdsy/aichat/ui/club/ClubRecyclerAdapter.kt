package com.cdsy.aichat.ui.club

import com.bumptech.glide.Glide
import com.cdsy.aichat.R
import com.cdsy.aichat.databinding.ItemClubBinding
import com.cdsy.aichat.model.api.club.ClubItem
import com.cdsy.aichat.ui.base.BaseRecyclerAdapter
import com.cdsy.aichat.util.LocalCountryData

/**
 * Club 列表适配器，继承项目的 BaseRecyclerAdapter。
 */
class ClubRecyclerAdapter :
    BaseRecyclerAdapter<ClubItem, ItemClubBinding>(R.layout.item_club) {

    override fun onCellClick(bean: ClubItem) {
        // 后续可接 Club 详情 / 发起通话 点击事件
    }

    override fun bindData(binding: ItemClubBinding, position: Int) {
        val item = baseList[position]
        binding.item = item
        val context = binding.root.context

        // 封面图
        Glide.with(context)
            .load(item.coverUrl)
            .placeholder(R.drawable.bg_cover_club_gradient)
            .into(binding.itemClubIv)

        // 名称 + 年龄
        binding.itemClubNameAge.text = item.displayNameAge
        binding.itemClubNameAge.isSelected = true

        // 地区：有 region 用国家映射 + 国旗 emoji，没有就用默认文案
        val region = item.user?.region.orEmpty()
        if (region.isEmpty()) {
            binding.itemClubIvEarth.visibility = android.view.View.VISIBLE
            binding.itemClubRegion.text = context.getString(R.string.profile_country_default)
        } else {
            binding.itemClubIvEarth.visibility = android.view.View.GONE
            val mapped = LocalCountryData.getCountryNameByIso(context, region)
            binding.itemClubRegion.text = if (mapped.isNotEmpty()) mapped else region
        }

        // 在线状态圆点：0 隐藏，其它展示（这里统一用绿色点，后续如需细分可再区分 label）
        if (item.onlineLabel == 0) {
            binding.viewOnline.visibility = android.view.View.GONE
        } else {
            binding.viewOnline.visibility = android.view.View.VISIBLE
            binding.viewOnline.setBackgroundResource(R.drawable.shape_radius_100_62d142)
        }

        // VIP 标识
        binding.vipLayout.visibility =
            if (item.isVip) android.view.View.VISIBLE else android.view.View.GONE

        binding.executePendingBindings()
    }
}
