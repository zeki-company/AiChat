package com.cdsy.aichat.ui.homepage

import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.ScaleAnimation
import android.view.animation.Animation.AnimationListener
import com.cdsy.aichat.R
import com.google.android.material.tabs.TabLayout

fun TabLayout.fixMargin() {
    post {
        for (i in 0 until tabCount) {
            val tab = (getChildAt(0) as ViewGroup).getChildAt(i)
            val params = tab.layoutParams as ViewGroup.MarginLayoutParams
            params.marginEnd = 12
            tab.layoutParams = params
            tab.requestLayout()
        }
    }
}

fun View.attachScaleAnimation() {
    // 创建放大动画
    val scaleUpAnimation = ScaleAnimation(
        1.0f, 1.2f,  // 从1.0倍放大到1.2倍
        1.0f, 1.2f,  // Y轴也同步放大
        Animation.RELATIVE_TO_SELF, 0.5f,  // 以自身中心为基准点
        Animation.RELATIVE_TO_SELF, 0.5f
    ).apply {
        duration = 1000 // 1秒
        fillAfter = true // 动画结束后保持状态
    }
    
    // 创建缩小动画
    val scaleDownAnimation = ScaleAnimation(
        1.2f, 1.0f,  // 从1.2倍缩小到1.0倍
        1.2f, 1.0f,  // Y轴也同步缩小
        Animation.RELATIVE_TO_SELF, 0.5f,  // 以自身中心为基准点
        Animation.RELATIVE_TO_SELF, 0.5f
    ).apply {
        duration = 1000 // 1秒
        fillAfter = true // 动画结束后保持状态
    }
    
    // 设置动画监听器，实现循环
    scaleUpAnimation.setAnimationListener(object : AnimationListener {
        override fun onAnimationStart(animation: Animation?) {}
        
        override fun onAnimationEnd(animation: Animation?) {
            // 放大动画结束后开始缩小动画
            startAnimation(scaleDownAnimation)
        }
        
        override fun onAnimationRepeat(animation: Animation?) {}
    })
    
    scaleDownAnimation.setAnimationListener(object : AnimationListener {
        override fun onAnimationStart(animation: Animation?) {}
        
        override fun onAnimationEnd(animation: Animation?) {
            // 缩小动画结束后开始放大动画，形成循环
            startAnimation(scaleUpAnimation)
        }
        
        override fun onAnimationRepeat(animation: Animation?) {}
    })
    
    // 开始动画
    startAnimation(scaleUpAnimation)
}