package com.cdsy.aichat.ui.view

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import com.cdsy.aichat.R

//通用的消息Dialog
class MessageDialog(
    context: Context,
    @StringRes val title: Int,
    @StringRes val message: Int,
    val onPrimary: () -> Unit,
    val onSecondary: () -> Unit
) : CommonDialog(
    context, R.layout.dialog_message
) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findViewById<TextView>(R.id.tv_title).text = context.getString(title)
        findViewById<TextView>(R.id.tv_message).text = context.getString(message)
        findViewById<TextView>(R.id.btn_primary).setOnClickListener {
            dismiss()
            onPrimary.invoke()
        }
        findViewById<TextView>(R.id.btn_secondary).setOnClickListener {
            dismiss()
            onSecondary.invoke()
        }
    }
}