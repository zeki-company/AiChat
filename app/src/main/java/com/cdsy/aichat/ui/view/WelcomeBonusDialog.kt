package com.cdsy.aichat.ui.view

import android.content.Context
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import com.cdsy.aichat.R

//首充奖励弹窗
class WelcomeBonusDialog(context: Context, val onClickNext: () -> Unit) :
    TransparentDialog(context, R.layout.dialog_welcome_bonus) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findViewById<ImageView>(R.id.iv_close).setOnClickListener { dismiss() }
        findViewById<FrameLayout>(R.id.btn_bonus_next).setOnClickListener { onClickNext() }
    }

    override fun show() {
        if (!isShowing) {
            super.show()
        }
    }

    override fun dismiss() {
        if (isShowing) {
            super.dismiss()
        }
    }

}