package com.cdsy.aichat.ui.setting

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Process
import androidx.appcompat.app.AlertDialog
import com.cdsy.aichat.R
import com.cdsy.aichat.databinding.FragmentSettingBinding
import com.cdsy.aichat.ui.activity.ContentActivity
import com.cdsy.aichat.ui.base.BaseFragment
import com.cdsy.aichat.ui.login.LoginActivity
import com.cdsy.aichat.util.LocaleHelper


class SettingFragment : BaseFragment<FragmentSettingBinding, SettingViewModel>(
    SettingViewModel::class.java,
    R.layout.fragment_setting
) {
    override fun initView() {
        binding.ivBack.setOnClickListener { activity?.finish() }
        binding.llLanguage.setOnClickListener {
            showLanguageSelectionDialog()
        }
        binding.llPrivacy.setOnClickListener { }
        binding.llNotification.setOnClickListener { }
        binding.llDeleteAccount.setOnClickListener { }
        binding.btnSignOut.setOnClickListener {
            viewModel.signOut()
        }
        viewModel.signOutEvent.observeNonNull {
            it.handleIfNot {
                // 跳转回登录页面
                val intent = Intent(requireContext(), LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }

        viewModel.languageChangeEvent.observeNonNull {
            it.handleIfNot { languageCode ->
                changeLanguage(requireContext(), languageCode)
                showRestartDialog()
            }
        }
    }

    override fun initData() {

    }

    private fun showLanguageSelectionDialog() {
        val languages = resources.getStringArray(R.array.language_options)
        val languageCodes = resources.getStringArray(R.array.language_codes)
        val currentLanguageCode = viewModel.getCurrentLanguageCode()
        val currentIndex = languageCodes.indexOf(currentLanguageCode).takeIf { it >= 0 } ?: 0

        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.language_selection_title))
            .setSingleChoiceItems(languages, currentIndex) { dialog, which ->
                val selectedLanguageCode = languageCodes[which]
                if (selectedLanguageCode != currentLanguageCode) {
                    viewModel.changeLanguage(selectedLanguageCode)
                }
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.dialog_cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showRestartDialog() {
        AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.language_restart_message))
            .setPositiveButton(getString(R.string.language_restart_ok)) { _, _ ->
                restartApp()
            }
            .setNegativeButton(getString(R.string.language_restart_cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(false)
            .show()
    }

    private fun restartApp() {
        val intent =
            requireContext().packageManager.getLaunchIntentForPackage(requireContext().packageName)
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent?.let { startActivity(it) }

        // 结束当前进程
        Process.killProcess(Process.myPid())
        System.exit(0)
    }

    fun changeLanguage(context: Context, languageCode: String) {
        LocaleHelper.setLocale(context, languageCode)
    }

}

fun Context.navigateToSetting() {
    startActivity(
        ContentActivity.createIntent(
            context = this,
            des = ContentActivity.Destination.Setting
        )
    )
}