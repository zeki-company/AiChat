package com.cdsy.aichat.ui.view

import android.content.Context
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.cdsy.aichat.R

//圆角卡片Dialog
abstract class CommonDialog(
    context: Context,
    private val resourceId: Int
) :
    TransparentDialog(
        context, R.layout.dialog_common
    ) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val flContainer = findViewById<FrameLayout>(R.id.fl_container)
        LayoutInflater.from(context).inflate(resourceId, flContainer, true)
    }

}