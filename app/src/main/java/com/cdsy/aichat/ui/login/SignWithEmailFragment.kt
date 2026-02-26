package com.cdsy.aichat.ui.login

import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.cdsy.aichat.R
import com.cdsy.aichat.databinding.FragmentSignWithEmailBinding
import com.cdsy.aichat.ui.base.BaseFragment

/**
 * 对应旧项目 SignWithEmailActivity：输入邮箱并发送验证码。
 * 这里先实现基本 UI + 发送验证码，后续再接 SignEmailCodeFragment。
 */
class SignWithEmailFragment : BaseFragment<FragmentSignWithEmailBinding, LoginViewModel>(
    LoginViewModel::class.java,
    R.layout.fragment_sign_with_email
) {

    override fun initView() {
        binding.btnContinue.isEnabled = false

        binding.etEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val email = s?.toString().orEmpty()
                binding.btnContinue.isEnabled = email.contains("@") && email.contains(".")
            }
        })

        binding.btnContinue.setOnClickListener {
            val email = binding.etEmail.text?.toString()?.trim().orEmpty()
            if (email.isEmpty()) return@setOnClickListener
            viewModel.sendEmailCaptcha(email)
        }

        // 监听验证码发送结果，由 ViewModel 统一处理 Service 调用
        viewModel.emailCaptchaEvent.observeNonNull { event ->
            event.handleIfNot { (email, key) ->
                Toast.makeText(requireContext(), R.string.unknown_unknown_check_email, Toast.LENGTH_SHORT).show()
                findNavController().navigate(
                    R.id.action_SignWithEmailFragment_to_SignEmailCodeFragment,
                    bundleOf(
                        "email" to email,
                        "key" to key
                    )
                )
            }
        }
    }

    override fun initData() {
        // 无需额外初始化数据
    }
}

