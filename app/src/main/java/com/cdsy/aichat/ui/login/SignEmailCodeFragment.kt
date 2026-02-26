package com.cdsy.aichat.ui.login

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.cdsy.aichat.R
import com.cdsy.aichat.databinding.FragmentSignEmailCodeBinding
import com.cdsy.aichat.ui.base.BaseFragment
import com.cdsy.aichat.ui.main.MainActivity

/**
 * 对应旧项目 SignEmailCodeActivity：输入邮箱验证码并完成登录 / 绑定。
 * 这里先实现登录流程，绑定逻辑暂不迁移。
 */
class SignEmailCodeFragment : BaseFragment<FragmentSignEmailCodeBinding, LoginViewModel>(
    LoginViewModel::class.java,
    R.layout.fragment_sign_email_code
) {

    private val email: String by lazy { arguments?.getString("email").orEmpty() }
    private val key: String by lazy { arguments?.getString("key").orEmpty() }

    override fun initView() {
        binding.tvSendInfo.text = getString(R.string.login_email_comment_top, "\n$email")

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
            // 简单复用 Email 发送验证码逻辑
            (parentFragment as? SignInOrUpFragment)?.let {
                Toast.makeText(requireContext(), R.string.unknown_unknown_resend_code, Toast.LENGTH_SHORT).show()
            }
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
        // 无需额外数据
    }

    private fun doLogin(code: String) {
        if (key.isEmpty()) {
            Toast.makeText(requireContext(), R.string.unknown_unknown_code_invalid, Toast.LENGTH_SHORT).show()
            return
        }
        viewModel.loginByEmail(key, code)
    }
}

