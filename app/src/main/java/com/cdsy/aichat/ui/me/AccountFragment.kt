package com.cdsy.aichat.ui.me

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.cdsy.aichat.R
import com.cdsy.aichat.databinding.DialogTopUpBinding
import com.cdsy.aichat.databinding.FragmentAccountBinding
import com.cdsy.aichat.manager.finance.GoogleBillingManager
import com.cdsy.aichat.manager.finance.GoogleBillingManager.fetchProductsInfo
import com.cdsy.aichat.manager.finance.GoogleBillingManager.startPurchase
import com.cdsy.aichat.manager.sharedpref.SharedPrefModel
import com.cdsy.aichat.ui.account_activity.navigateToAccountActivity
import com.cdsy.aichat.ui.base.BaseFragment
import com.cdsy.aichat.ui.setting.navigateToSetting
import com.google.android.material.bottomsheet.BottomSheetDialog
import timber.log.Timber

class AccountFragment : BaseFragment<FragmentAccountBinding, AccountViewModel>(
    AccountViewModel::class.java,
    R.layout.fragment_account
) {

    val topUpDialog by lazy { BottomSheetDialog(requireContext(), R.style.BottomSheetDialogStyle) }
    val topUpDialogBinding by lazy { DialogTopUpBinding.inflate(LayoutInflater.from(requireContext())) }
    override fun initView() {
        binding.ivSetting.setOnClickListener {
            context?.navigateToSetting()
        }
        binding.cslTopUp.setOnClickListener {
            showTopUpSheetDialog()
        }
        binding.srl.setOnRefreshListener {
            viewModel.fetchUserInfo()
            viewModel.fetchGoodsList()
        }
        binding.llAccountActivity.setOnClickListener {
            requireContext().navigateToAccountActivity()
        }
        viewModel.isRefreshing.observeNonNull {
            binding.srl.isRefreshing = it
        }
        viewModel.goodsList.observeNonNull {
            topUpDialogBinding.rvTopUp.apply {
                layoutManager = GridLayoutManager(requireContext(), 2)
                adapter = TopUpRecyclerAdapter { goods ->
                    //todo 充值
                    goods.fetchProductsInfo { productInGoogle ->
                        Timber.d("Google 小票 $productInGoogle")
                        GoogleBillingManager.setUpUIListener(
                            viewModel.progressDialog,
                            viewModel.apiErrorEvent
                        )
                        activity?.startPurchase(productInGoogle, viewModel.financeService)
                    }
                }.apply {
                    replaceData(it)
                }
            }
        }
        viewModel.userInfo.observeNonNull {
            binding.tvUserId.text = "UserID:${it.id}"
            binding.tvToken.text = "${it.balance.credit} TOKEN"
        }
    }

    override fun initData() {
        viewModel.name.value = SharedPrefModel.getUserModel().name
        viewModel.portrait.value = SharedPrefModel.getUserModel().portrait
        viewModel.fetchGoodsList()
        viewModel.fetchUserInfo()
    }

    //弹出登陆弹窗
    private fun showTopUpSheetDialog() {
        topUpDialogBinding.ivClose.setOnClickListener { topUpDialog.dismiss() }
        topUpDialog.setContentView(topUpDialogBinding.root)
        topUpDialog.setCancelable(true)
        topUpDialog.window?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            ?.setBackgroundColor(Color.TRANSPARENT)
        topUpDialog.window?.setDimAmount(0f)
        topUpDialog.show()
    }

}