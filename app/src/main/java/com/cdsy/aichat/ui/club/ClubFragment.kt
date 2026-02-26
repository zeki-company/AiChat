package com.cdsy.aichat.ui.club

import androidx.recyclerview.widget.GridLayoutManager
import com.cdsy.aichat.R
import com.cdsy.aichat.databinding.FragmentClubBinding
import com.cdsy.aichat.ui.base.BaseFragment
import com.cdsy.aichat.ui.homepage.ClubViewModel
import com.cdsy.aichat.util.addBottomSpace
import com.cdsy.aichat.util.setOnLoadMoreListener
/**
 * MVVM 版 ClubFragment：
 * - 使用旧项目 v1/club/search 与 v1/video/call/coin 接口
 * - 页面结构参考旧 ClubFragment，数据流走 ClubViewModel
 */
class ClubFragment : BaseFragment<FragmentClubBinding, ClubViewModel>(
    ClubViewModel::class.java,
    R.layout.fragment_club
) {

    override fun initView() {
        binding.viewModel = viewModel

        binding.rvClub.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = ClubRecyclerAdapter()
            addBottomSpace()
        }

        binding.srl.setOnRefreshListener {
            viewModel.refreshClubList()
        }

        binding.rvClub.setOnLoadMoreListener {
            viewModel.loadMoreClubList()
        }

        viewModel.isRefreshing.observeNonNull {
            binding.srl.isRefreshing = it
        }

        // 余额 -> 顶部 coins 文案
        viewModel.balance.observeNonNull {
            //binding.tvCoins.text = it.coins.toString()
        }

        // 列表数据
        viewModel.clubList.observeNonNull {
            (binding.rvClub.adapter as ClubRecyclerAdapter).replaceData(it)
        }

        // 新用户优惠 Banner
        viewModel.newBonusInfo.observeNonNull { info ->
            if (info.privateCallDiscount && info.privateCallDiscountExpireTime != null) {
                binding.cardNewBonus.visibility = android.view.View.VISIBLE
                binding.clubBuyCoinsLayout.visibility = android.view.View.GONE
                val percent = info.privateCallDiscountPercent ?: ""
                binding.tvNewBonusContent.text =
                    getString(R.string.banner_club_new_bonus, percent)
            } else {
                binding.cardNewBonus.visibility = android.view.View.GONE
            }
        }
    }

    override fun initData() {
        viewModel.fetchBalance()
        viewModel.refreshClubList()
        viewModel.fetchNewBonusInfo()
    }
}

