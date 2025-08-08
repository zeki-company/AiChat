package com.cdsy.aichat.ui.splash

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.cdsy.aichat.R
import com.cdsy.aichat.databinding.ActivitySplashBinding
import com.cdsy.aichat.ui.base.BaseActivity


class SplashActivity : BaseActivity() {

    companion object {
        private const val REQUEST_CAMERA_STORAGE = 1
    }

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
    }



}