package com.cdsy.aichat.ui.homepage

import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.cdsy.aichat.R
import com.cdsy.aichat.databinding.FragmentHomepageBinding
import com.cdsy.aichat.ui.base.BaseFragment
import com.cdsy.aichat.ui.character_detail.navigateToCharacterDetail
import com.cdsy.aichat.ui.search.navigateToSearch
import com.cdsy.aichat.ui.view.WelcomeBonusDialog
import com.cdsy.aichat.util.addBottomSpace
import com.cdsy.aichat.util.setOnLoadMoreListener
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomepageFragment : BaseFragment<FragmentHomepageBinding, HomepageViewModel>(
    HomepageViewModel::class.java, R.layout.fragment_homepage
) {

    val welcomeBonusDialog: WelcomeBonusDialog by lazy {
        WelcomeBonusDialog(requireContext()) {
            //todo next bonus
        }
    }

    override fun initView() {
        setupTabLayout()
        setupChatterList()
        binding.srl.setOnRefreshListener {
            viewModel.fetchCharacterList()
        }
        binding.ivSearch.setOnClickListener {
            requireContext().navigateToSearch()
        }
        binding.rvChatter.setOnLoadMoreListener {
            viewModel.loadMoreCharacterList()
        }
        binding.ivReward.setOnClickListener {
            welcomeBonusDialog.show()
        }
        viewModel.isRefreshing.observeNonNull {
            binding.srl.isRefreshing = it
        }
        viewModel.tabList.observeNonNull {
            it.forEach { tab ->
                binding.tl.addTab(binding.tl.newTab().apply {
                    setText(tab.name)
                })
            }
            binding.tl.fixMargin()
        }
        viewModel.characterList.observeNonNull {
            (binding.rvChatter.adapter as MutiChatterRecyclerAdapter).replaceData(it)
        }
        viewModel.currentTag.observeNonNull {
            viewModel.fetchCharacterList()
        }
        lifecycleScope.launch {
            delay(1000)
            binding.ivReward.attachScaleAnimation()
        }
    }

    override fun initData() {
        viewModel.fetchTabList()
        viewModel.fetchBalance()
    }

    private fun setupChatterList() {
        binding.rvChatter.apply {
            adapter = MutiChatterRecyclerAdapter(lifecycle) {
                requireContext().navigateToCharacterDetail(it.id)
            }
            layoutManager = GridLayoutManager(requireContext(), 2).apply {
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return if ((adapter as MutiChatterRecyclerAdapter).getItemViewType(position) == TYPE_ADS_BANNER) 2 else 1
                    }
                }
            }
            addBottomSpace()
        }
    }

    private fun setupTabLayout() {
        binding.tl.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewModel.currentTag.value =
                    viewModel.tabList.value?.first { it.name == tab?.text.toString() }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
    }

}