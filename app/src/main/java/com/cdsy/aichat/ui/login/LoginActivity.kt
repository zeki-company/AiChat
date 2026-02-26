package com.cdsy.aichat.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cdsy.aichat.MainApplication
import com.cdsy.aichat.R
import com.cdsy.aichat.manager.sharedpref.SharedPrefModel
import com.cdsy.aichat.ui.main.MainActivity

/**
 * LoginActivity 现在仅作为登录流程的单 Activity 容器，
 * 具体 UI 和逻辑放在 Navigation + Fragment 中（SignInOrUpFragment 等）。
 */
class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (SharedPrefModel.userId.isNotEmpty()) {
            MainApplication.nowUserId = SharedPrefModel.userId
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            setContentView(R.layout.activity_login)
        }
    }
}
