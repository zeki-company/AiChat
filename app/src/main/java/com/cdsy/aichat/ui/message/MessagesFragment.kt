package com.cdsy.aichat.ui.message

import android.widget.TextView
import com.cdsy.aichat.R
import com.cdsy.aichat.databinding.FragmentMessagesBinding
import com.cdsy.aichat.ui.base.BaseFragment
import com.google.android.material.tabs.TabLayoutMediator

class MessagesFragment : BaseFragment<FragmentMessagesBinding, MessageViewModel>(
    MessageViewModel::class.java,
    R.layout.fragment_messages
) {

    private lateinit var pagerAdapter: MessageListPagerAdapter

    override fun initView() {
        setupViewPager()
        setupTabLayout()
    }

    override fun initData() {
        // 初始化数据
    }

    private fun setupViewPager() {
        pagerAdapter = MessageListPagerAdapter(this)
        binding.viewpager.adapter = pagerAdapter
    }

    private fun setupTabLayout() {
        // 使用TabLayoutMediator关联TabLayout和ViewPager2
        TabLayoutMediator(binding.tlMessage, binding.viewpager) { tab, position ->
            tab.setCustomView(R.layout.tab_item)
            tab.customView?.findViewById<TextView>(R.id.tab_title)?.text =
                pagerAdapter.getTabTitle(position)
        }.attach()
    }
}