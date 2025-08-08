package com.cdsy.aichat.ui.login

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.cdsy.aichat.MainApplication
import com.cdsy.aichat.R
import com.cdsy.aichat.databinding.ActivityLoginBinding
import com.cdsy.aichat.databinding.DialogLoginBinding
import com.cdsy.aichat.manager.api.GoogleSignInService
import com.cdsy.aichat.manager.sharedpref.SharedPrefModel
import com.cdsy.aichat.ui.base.BindingActivity
import com.cdsy.aichat.ui.main.MainActivity
import com.cdsy.aichat.util.DeviceUtil
import com.google.android.material.bottomsheet.BottomSheetDialog

class LoginActivity : BindingActivity<ActivityLoginBinding, LoginViewModel>(
    R.layout.activity_login,
    LoginViewModel::class.java
) {

    val loginDialog by lazy { BottomSheetDialog(this, R.style.BottomSheetDialogStyle) }
    val loginDialogBinding by lazy { DialogLoginBinding.inflate(LayoutInflater.from(this)) }


    override fun initView() {
        if (SharedPrefModel.userId.isNotEmpty()) {
            MainApplication.nowUserId = SharedPrefModel.userId
            finish()
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            binding.root
            viewModel.showLoginEvent.observeNonNull { it.handleIfNot { showLoginSheetDialog() } }
            viewModel.googleSignInIntentEvent.observeNonNull {
                it.handleIfNot { e ->
                    startActivityForResult(
                        e,
                        GoogleSignInService.RC_SIGN_IN
                    )
                }
            }
            viewModel.googleSignInSuccessEvent.observeNonNull {
                it.handleIfNot {
                    if (it) onGoogleSignInSuccess()
                }
            }
            viewModel.googleSignInErrorEvent.observeNonNull {
                it.handleIfNot { e ->
                    onGoogleSignInError(
                        e
                    )
                }
            }
        }
    }

    override fun initData() {
        viewModel.initDevice(DeviceUtil.getDeviceInfo(this))
    }

    override fun onStop() {
        super.onStop()
        loginDialog.dismiss()
    }


    //弹出登陆弹窗
    private fun showLoginSheetDialog() {
        loginDialog.setContentView(loginDialogBinding.root)
        loginDialog.setCancelable(false)
        loginDialog.window?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            ?.setBackgroundColor(Color.TRANSPARENT)
        loginDialog.window?.setDimAmount(0f)
        loginDialog.show()
        loginDialogBinding.btnGoogle.setOnClickListener {
            viewModel.startGoogleSignIn()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GoogleSignInService.RC_SIGN_IN) {
            viewModel.handleGoogleSignInResult(data)
        }
    }

    private fun onGoogleSignInSuccess() {
        // Google登录成功，跳转到主界面
        finish()
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun onGoogleSignInError(errorMessage: String) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        showLoginSheetDialog()
    }

}