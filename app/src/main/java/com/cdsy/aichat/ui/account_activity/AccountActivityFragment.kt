package com.cdsy.aichat.ui.account_activity

import android.content.Context
import com.cdsy.aichat.R
import com.cdsy.aichat.databinding.FragmentAccountActivityBinding
import com.cdsy.aichat.ui.base.BaseFragment

class AccountActivityFragment :
    BaseFragment<FragmentAccountActivityBinding, AccountActivityViewModel>(
        AccountActivityViewModel::class.java,
        R.layout.fragment_account_activity
    ) {
    override fun initView() {
        binding.ivBack.setOnClickListener {
            activity?.finish()
        }
    }

    override fun initData() {

    }
}

fun Context.navigateToAccountActivity() {
    startActivity(
        com.cdsy.aichat.ui.activity.ContentActivity.createIntent(
            context = this,
            des = com.cdsy.aichat.ui.activity.ContentActivity.Destination.AccountActivity
        )
    )
}