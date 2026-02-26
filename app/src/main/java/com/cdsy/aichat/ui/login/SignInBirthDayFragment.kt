package com.cdsy.aichat.ui.login

import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import androidx.navigation.fragment.findNavController
import com.cdsy.aichat.R
import com.cdsy.aichat.databinding.FragmentSignInBirthdayBinding
import com.cdsy.aichat.ui.base.BaseFragment
import java.util.Calendar

/**
 * 新增：生日选择 Fragment，对应旧项目中生日步骤。
 * 这里只负责本地收集生日数据，后续可接 LoginService.updateUserInfo。
 */
class SignInBirthDayFragment : BaseFragment<FragmentSignInBirthdayBinding, LoginViewModel>(
    LoginViewModel::class.java,
    R.layout.fragment_sign_in_birthday
) {

    private val name: String by lazy { arguments?.getString("name").orEmpty() }
    private val gender: Int by lazy { arguments?.getInt("gender", -1) ?: -1 }
    private val ageInit: Int by lazy { arguments?.getInt("age", -1) ?: -1 }
    private val birthdayInit: Long by lazy { arguments?.getLong("birthday", -1L) ?: -1L }

    override fun initView() {
        val calendar = Calendar.getInstance()
        if (birthdayInit > 0) {
            calendar.timeInMillis = birthdayInit
        }
        binding.datePicker.init(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH),
            null
        )

        binding.btnContinue.setOnClickListener {
            val cal = Calendar.getInstance()
            cal.set(binding.datePicker.year, binding.datePicker.month, binding.datePicker.dayOfMonth, 0, 0, 0)
            val birthday = cal.timeInMillis
            // TODO: 调用 LoginService.updateUserInfo 上报 name/gender/birthday 等信息
            findNavController().navigateUp()
        }
    }

    override fun initData() {
    }
}

