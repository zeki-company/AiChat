package com.cdsy.aichat.ui.search

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.cdsy.aichat.R
import com.cdsy.aichat.databinding.FragmentSearchBinding
import com.cdsy.aichat.ui.activity.ContentActivity
import com.cdsy.aichat.ui.base.BaseFragment
import com.cdsy.aichat.ui.character_detail.navigateToCharacterDetail
import com.cdsy.aichat.ui.homepage.ChatterTagRecyclerAdapter
import com.cdsy.aichat.ui.homepage.MutiChatterRecyclerAdapter
import com.cdsy.aichat.util.setOnLoadMoreListener
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager

class SearchFragment : BaseFragment<FragmentSearchBinding, SearchViewModel>(
    SearchViewModel::class.java,
    R.layout.fragment_search
) {
    override fun initView() {
        //setupTransparent()
        binding.rvTag.apply {
            layoutManager = FlexboxLayoutManager(requireContext()).apply {
                flexDirection = FlexDirection.ROW
            }
            adapter = ChatterTagRecyclerAdapter {
                viewModel.keyword.value = it.tag_name
                fetchChatterList()
            }
        }
        binding.rvChatterPoster.setOnLoadMoreListener {
            viewModel.loadMoreCharacterList()
        }
        binding.rvChatterPoster.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = MutiChatterRecyclerAdapter(lifecycle) {
                requireContext().navigateToCharacterDetail(it.id)
            }
        }
        binding.srl.setOnRefreshListener { fetchChatterList() }
        binding.ivBack.setOnClickListener { activity?.finish() }
        binding.ivSearch.setOnClickListener { fetchChatterList() }
        viewModel.isRefreshing.observeNonNull {
            binding.srl.isRefreshing = it
        }
        viewModel.popularList.observeNonNull {
            (binding.rvTag.adapter as ChatterTagRecyclerAdapter).replaceData(it)
        }
        viewModel.characterList.observeNonNull {
            (binding.rvChatterPoster.adapter as MutiChatterRecyclerAdapter).replaceData(it)
        }
    }

    override fun initData() {
        viewModel.fetchPopularList()
    }

    private fun fetchChatterList() {
        binding.srl.visibility = View.VISIBLE
        binding.tvTitle.visibility = View.GONE
        binding.rvTag.visibility = View.GONE
        viewModel.fetchCharacterList()
    }
}

fun Context.navigateToSearch() {
    startActivity(
        ContentActivity.createIntent(
            context = this,
            des = ContentActivity.Destination.Search
        )
    )
}