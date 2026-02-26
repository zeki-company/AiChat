package com.cdsy.aichat.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.core.os.bundleOf
import com.cdsy.aichat.R
import com.cdsy.aichat.databinding.FragmentSignInNameBinding
import com.cdsy.aichat.ui.base.BaseFragment

/**
 * 对应旧项目 SignInNameActivity：输入昵称，后续可扩展到性别 / 生日 / 语言等。
 */
class SignInNameFragment : BaseFragment<FragmentSignInNameBinding, LoginViewModel>(
    LoginViewModel::class.java,
    R.layout.fragment_sign_in_name
) {

    private val initName: String by lazy { arguments?.getString("name").orEmpty() }
    private val gender: Int by lazy { arguments?.getInt("gender", -1) ?: -1 }
    private val age: Int by lazy { arguments?.getInt("age", -1) ?: -1 }
    private val birthday: Long by lazy { arguments?.getLong("birthday", -1L) ?: -1L }

    override fun initView() {
        binding.etName.setText(initName)
        updateButtonState(initName.length)

        binding.etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                updateButtonState(s?.length ?: 0)
            }
        })

        binding.btnContinue.setOnClickListener {
            val name = binding.etName.text?.toString()?.trim().orEmpty()
            if (name.isEmpty()) return@setOnClickListener
            // 这里先直接进入生日 Fragment，后续可根据 gender / age 决定路由
            findNavController().navigate(
                R.id.action_SignInNameFragment_to_SignInBirthDayFragment,
                bundleOf(
                    "name" to name,
                    "gender" to gender,
                    "age" to age,
                    "birthday" to birthday
                )
            )
        }
    }

    override fun initData() {
    }

    private fun updateButtonState(len: Int) {
        binding.btnContinue.isEnabled = len in 1..30
    }
}

