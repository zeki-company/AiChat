package com.cdsy.aichat.ui.login

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.cdsy.aichat.R
import com.cdsy.aichat.databinding.FragmentSignPhoneCodeBinding
import com.cdsy.aichat.ui.base.BaseFragment
import com.cdsy.aichat.ui.main.MainActivity

/**
 * 对应旧项目 SignPhoneCodeActivity：输入手机验证码并完成登录。
 * 同样使用 key.code 构建 token。
 */
class SignPhoneCodeFragment : BaseFragment<FragmentSignPhoneCodeBinding, LoginViewModel>(
    LoginViewModel::class.java,
    R.layout.fragment_sign_phone_code
) {

    private val phone: String by lazy { arguments?.getString("phone").orEmpty() }
    private val callCode: String by lazy { arguments?.getString("callCode").orEmpty() }
    private val key: String by lazy { arguments?.getString("key").orEmpty() }

    override fun initView() {
        binding.tvSendInfo.text =
            getString(R.string.login_email_comment_top) + "\n+$callCode $phone"

        binding.etCode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val code = s?.toString().orEmpty()
                if (code.length == 6) {
                    doLogin(code)
                }
            }
        })

        binding.tvResend.setOnClickListener {
            Toast.makeText(requireContext(), R.string.unknown_unknown_resend_code, Toast.LENGTH_SHORT).show()
            // TODO: 可复用 sendPhoneCaptcha 再发一遍
        }

        // 监听登录成功事件
        viewModel.loginSuccessEvent.observeNonNull { event ->
            event.handleIfNot {
                startActivity(Intent(requireContext(), MainActivity::class.java))
                requireActivity().finish()
            }
        }
    }

    override fun initData() {
    }

    private fun doLogin(code: String) {
        if (key.isEmpty()) {
            Toast.makeText(requireContext(), R.string.unknown_unknown_code_invalid, Toast.LENGTH_SHORT).show()
            return
        }
        viewModel.loginByPhone(key, code)
    }
}

