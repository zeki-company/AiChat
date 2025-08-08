package com.cdsy.aichat.ui.character_detail

import android.content.Context
import android.graphics.Color
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import com.cdsy.aichat.R
import com.cdsy.aichat.databinding.FragmentCharacterDetailBinding
import com.cdsy.aichat.ui.activity.ContentActivity
import com.cdsy.aichat.ui.base.BaseFragment
import com.cdsy.aichat.ui.chat.navigateToChat
import com.cdsy.aichat.ui.homepage.ChatterTagRecyclerAdapter
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager

const val KEY_CHARACTER_ID = "key_character_id"
const val KEY_CREDIT = "key_credit"
const val KEY_CHARACTER_DETAIL = "key_character_detail"

class CharacterDetailFragment :
    BaseFragment<FragmentCharacterDetailBinding, CharacterDetailViewModel>(
        CharacterDetailViewModel::class.java,
        R.layout.fragment_character_detail
    ) {

    val characterId by lazy { arguments?.getString(KEY_CHARACTER_ID) }


    override fun initView() {
        activity?.let {
            // 沉浸式状态栏和导航栏
            WindowCompat.setDecorFitsSystemWindows(it.window, false)
            it.window.statusBarColor = Color.TRANSPARENT
            it.window.navigationBarColor = Color.TRANSPARENT

            // 如果需要让内容延伸到状态栏下方
            ViewCompat.getWindowInsetsController(it.window.decorView)?.apply {
                isAppearanceLightStatusBars = false // 状态栏字体黑色，false为白色
                isAppearanceLightNavigationBars = false // 导航栏字体黑色
            }
        }


        binding.ivBack.setOnClickListener { activity?.finish() }

        // 设置关于箭头的点击事件
        binding.ivAboutArrow.setOnClickListener {
            viewModel.isTextExpanded.value = !viewModel.isTextExpanded.value!!
        }
        binding.rvTag.apply {
            layoutManager = FlexboxLayoutManager(context).apply {
                flexDirection = FlexDirection.ROW
            }
            adapter = ChatterTagRecyclerAdapter {}
        }

        binding.viewGoChat.setOnClickListener {
            viewModel.characterDetail.value?.let { characterDetail ->
                requireContext().navigateToChat(
                    credit = viewModel.credit.value ?: 0, characterDetail = characterDetail
                )
            }
        }

        viewModel.characterDetail.observeNonNull {
            (binding.rvTag.adapter as ChatterTagRecyclerAdapter).replaceData(it.tags)
        }

        viewModel.isTextExpanded.observeNonNull {
            toggleAboutText(it)
        }
    }

    /**
     * 切换关于文本的显示状态
     */
    private fun toggleAboutText(textExpanded: Boolean) {
        if (textExpanded) {
            // 显示全部文本
            binding.tvAboutText.maxLines = 1000
            binding.ivAboutArrow.rotation = 0f
        } else {
            // 限制为最多4行
            binding.tvAboutText.maxLines = 4
            binding.ivAboutArrow.rotation = 90f
        }
    }

    override fun initData() {
        characterId?.let { viewModel.fetchCharacterDetail(it) }
    }

}

fun Context.navigateToCharacterDetail(id: String) {
    startActivity(
        ContentActivity.createIntent(
            context = this,
            des = ContentActivity.Destination.CharacterDetail,
            bundle = bundleOf(Pair(KEY_CHARACTER_ID, id))
        )
    )
}