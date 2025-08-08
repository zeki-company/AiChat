package com.cdsy.aichat.ui.view

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import com.cdsy.aichat.R

class MessagePopupDialog(
    private val context: Context,
    private val anchorView: View,
    private val onPinClick: () -> Unit,
    private val onDeleteClick: () -> Unit
) : PopupWindow(
    LayoutInflater.from(context).inflate(R.layout.dialog_message_popup, null, false),
    ViewGroup.LayoutParams.WRAP_CONTENT,
    ViewGroup.LayoutParams.WRAP_CONTENT,
    true
) {
    private fun setupPopup() {
        isFocusable = true
        isOutsideTouchable = true
        isTouchable = true
        elevation = 8f
        setBackgroundDrawable(android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT))

        // 设置动画
        animationStyle = android.R.style.Animation_Dialog

        // 设置点击事件
        contentView.findViewById<LinearLayout>(R.id.btn_pin).setOnClickListener {
            onPinClick()
            dismiss()
        }

        contentView.findViewById<LinearLayout>(R.id.btn_delete).setOnClickListener {
            onDeleteClick()
            dismiss()
        }
    }

    fun show() {
        setupPopup()

        // 计算显示位置 - 在item中间靠右的位置
        val location = IntArray(2)
        anchorView.getLocationInWindow(location)

        val anchorWidth = anchorView.width
        val anchorHeight = anchorView.height

        // 在item中间的位置显示
        val x = location[0] + anchorWidth / 2
        val y = location[1] + anchorHeight / 2

        // 确保弹出窗口不会超出屏幕边界
        val screenWidth = context.resources.displayMetrics.widthPixels
        val screenHeight = context.resources.displayMetrics.heightPixels

        val finalX = when {
            x < 0 -> 16
            x > screenWidth -> screenWidth - 200 // 假设弹出窗口宽度约为200dp
            else -> x
        }

        val finalY = when {
            y < 0 -> 16
            y > screenHeight -> screenHeight - 100 // 假设弹出窗口高度约为100dp
            else -> y
        }
        showAtLocation(anchorView, Gravity.NO_GRAVITY, finalX, finalY)
    }
} 