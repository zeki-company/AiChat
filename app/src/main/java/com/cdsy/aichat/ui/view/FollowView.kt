package com.cdsy.aichat.ui.view

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.widget.AppCompatImageView
import kotlin.jvm.JvmOverloads
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewGroup
import com.cdsy.aichat.R
import timber.log.Timber
import kotlin.math.abs

/**
 * @Author: ZEKI https://github.com/ZYF99
 * @Date: 2021/6/6 8:17 上午
 */
class FollowView : AppCompatImageView {

    //最小移动距离（MOVE中低于此滑动距离不将其视为滑动事件）
    private val minDistance = 10

    private var mPaint: Paint? = null
    private var follow = 1 // 1：跟随X轴  2：跟随Y轴  3：跟随X与Y轴

    constructor(context: Context?) : super(context!!) {
        initViews()
    }

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int = 0) : super(context, attrs, defStyleAttr) {
        obtainStyledAttrs(context, attrs, defStyleAttr)
        initViews()
    }

    private fun obtainStyledAttrs(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.FollowView, defStyleAttr, 0)
        follow = if (a.hasValue(R.styleable.FollowView_follow)) a.getInt(R.styleable.FollowView_follow, 1) else 1
        a.recycle()
    }

    private fun initViews() {
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)

    }

    private var startX1 = 0f
    private var startY1 = 0f
    private var endX1 = 0f
    private var endY1 = 0f


    private var distanceToTop = 0f
    private var distanceToLeft = 0f

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Timber.d("parent w" + (parent as? ViewGroup)?.width)
        Timber.d("parent y" + (parent as? ViewGroup)?.height)
        Timber.d("现在View位置" + y)
        Timber.d("触碰位置" + event?.rawY)

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                startX1 = event.rawX
                startY1 = event.rawY
                distanceToTop = event.y
                distanceToLeft = event.x
                /*Timber.d("触碰点距上$distanceToTop")
                Timber.d("触碰点距左" + distanceToLeft)*/
            }
            MotionEvent.ACTION_MOVE -> {
                val nowX = event.rawX - distanceToLeft
                val nowY = event.rawY - distanceToTop
                when (follow) {
                    1 -> {
                        if (abs(nowX - startX1) >= minDistance) {
                            //跟随X
                            this.x = nowX
                        }
                    }
                    2 -> {
                        if (abs(nowY - startY1) >= minDistance) {
                            //跟随Y
                            this.y = nowY
                        }
                    }
                    3 -> {
                        if (abs(nowX - startX1) >= minDistance && abs(nowY - startY1) >= minDistance) {
                            //自由跟踪
                            this.x = nowX
                            this.y = nowY
                        }
                    }
                }
                val xLimit = ((parent as? ViewGroup)?.width ?: 0) - width
                val yLimit = ((parent as? ViewGroup)?.height ?: 0) - height
                when {
                    this.x < 0 -> this.x = 0f
                    this.x > xLimit -> this.x = xLimit.toFloat()
                    this.y < 0 -> this.y = 0f
                    this.y > yLimit -> this.y = yLimit.toFloat()
                }
            }
            MotionEvent.ACTION_UP -> {
                endX1 = event.rawX //记录结束位置
                endY1 = event.rawY //记录结束位置
            }
        }

        return when (follow) {
            1 -> {
                if (endX1 - startX1 < minDistance) super.onTouchEvent(event) else true
            }
            2 -> {
                if (endY1 - startY1 < minDistance) super.onTouchEvent(event) else true
            }
            3 -> {
                if (endX1 - startX1 > minDistance || endY1 - startY1 > minDistance) true else super.onTouchEvent(event)
            }
            else -> super.onTouchEvent(event)
        }
    }
}