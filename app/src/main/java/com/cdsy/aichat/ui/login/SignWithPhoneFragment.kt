package com.cdsy.aichat.ui.login

import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.cdsy.aichat.R
import com.cdsy.aichat.databinding.FragmentSignWithPhoneBinding
import com.cdsy.aichat.ui.base.BaseFragment

/**
 * 对应旧项目 SignWithPhoneActivity：输入手机号并发送验证码。
 * 简化版本：手动输入区号和手机号，不做国家列表。
 */
class SignWithPhoneFragment : BaseFragment<FragmentSignWithPhoneBinding, LoginViewModel>(
    LoginViewModel::class.java,
    R.layout.fragment_sign_with_phone
) {

    override fun initView() {
        binding.btnContinue.isEnabled = false

        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val code = binding.etCode.text?.toString().orEmpty()
                val phone = binding.etPhone.text?.toString().orEmpty()
                binding.btnContinue.isEnabled = code.isNotEmpty() && phone.length >= 6
            }
        }
        binding.etCode.addTextChangedListener(watcher)
        binding.etPhone.addTextChangedListener(watcher)

        binding.btnContinue.setOnClickListener {
            val callCode = binding.etCode.text?.toString()?.trim().orEmpty()
            val phone = binding.etPhone.text?.toString()?.trim().orEmpty()
            if (callCode.isEmpty() || phone.isEmpty()) return@setOnClickListener
            viewModel.sendPhoneCaptcha(callCode, phone)
        }

        // 监听手机验证码发送结果
        viewModel.phoneCaptchaEvent.observeNonNull { event ->
            event.handleIfNot { (phone, callCode, key) ->
                Toast.makeText(requireContext(), R.string.dlg_ver_code_sent_success, Toast.LENGTH_SHORT).show()
                findNavController().navigate(
                    R.id.action_SignWithPhoneFragment_to_SignPhoneCodeFragment,
                    bundleOf(
                        "phone" to phone,
                        "callCode" to callCode,
                        "key" to key
                    )
                )
            }
        }
    }

    override fun initData() {
    }

}

