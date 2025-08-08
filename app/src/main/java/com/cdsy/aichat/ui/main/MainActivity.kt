package com.cdsy.aichat.ui.main

import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.cdsy.aichat.R
import com.cdsy.aichat.databinding.ActivityMainBinding
import com.cdsy.aichat.ui.base.BindingActivity
import android.widget.TextView

class MainActivity : BindingActivity<ActivityMainBinding, MainViewModel>(
    R.layout.activity_main,
    MainViewModel::class.java
) {

    private lateinit var currentNavController: LiveData<NavController>

    override fun initView() {
        binding.clRoot
        setupBottomNavigation()
    }

    override fun initData() {
        viewModel.checkUnSubmitReceiptByTimer()
    }

    private fun setupBottomNavigation() {
        val listOfMainGraph = listOf(
            R.navigation.top_homepage,
            R.navigation.top_messages,
            R.navigation.top_account
        )

        // 设置点击事件
        binding.navHome.setOnClickListener {
            switchFragment(0, listOfMainGraph)
            updateBottomNavigation(0)
        }

        binding.navMessages.setOnClickListener {
            switchFragment(1, listOfMainGraph)
            updateBottomNavigation(1)
        }

        binding.navAccount.setOnClickListener {
            switchFragment(2, listOfMainGraph)
            updateBottomNavigation(2)
        }

        // 初始化选中状态
        updateBottomNavigation(0)
        switchFragment(0, listOfMainGraph)
    }

    private fun switchFragment(position: Int, navGraphIds: List<Int>) {
        val fragmentManager = supportFragmentManager
        val fragmentTag = "bottomNavigation#$position"

        // 获取或创建NavHostFragment
        var navHostFragment = fragmentManager.findFragmentByTag(fragmentTag) as? NavHostFragment
        if (navHostFragment == null) {
            navHostFragment = NavHostFragment.create(navGraphIds[position])
            fragmentManager.beginTransaction()
                .add(R.id.nav_host_container, navHostFragment, fragmentTag)
                .commitNow()
        }

        // 隐藏所有其他fragment
        navGraphIds.forEachIndexed { index, _ ->
            if (index != position) {
                val otherFragment = fragmentManager.findFragmentByTag("bottomNavigation#$index")
                otherFragment?.let {
                    fragmentManager.beginTransaction().hide(it).commitNow()
                }
            }
        }

        // 显示选中的fragment
        navHostFragment.let {
            fragmentManager.beginTransaction().show(it).commitNow()
        }
    }

    private fun updateBottomNavigation(selectedPosition: Int) {
        val positions = listOf(
            binding.navHome,
            binding.navMessages,
            binding.navAccount
        )

        positions.forEachIndexed { index, layout ->
            val iconView = layout.getChildAt(0) as ImageView
            val textView = layout.getChildAt(1) as TextView

            if (index == selectedPosition) {
                iconView.setColorFilter(ContextCompat.getColor(this, R.color.accentColor))
                textView.setTextColor(ContextCompat.getColor(this, R.color.accentColor))
            } else {
                iconView.setColorFilter(ContextCompat.getColor(this, android.R.color.white))
                textView.setTextColor(ContextCompat.getColor(this, android.R.color.white))
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}
