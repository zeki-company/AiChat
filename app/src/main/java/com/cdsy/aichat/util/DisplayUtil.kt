package com.cdsy.aichat.util

import android.content.Context
import android.util.TypedValue
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.cdsy.aichat.R
import com.cdsy.aichat.ui.view.CornerImageView
import java.math.RoundingMode
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * @Author: ZEKI https://github.com/ZYF99
 * @Date: 2021/7/29 9:36 上午
 */

@BindingAdapter("timeHM")
fun loadTimeHM(textView: TextView, time: Long) {
    val format = SimpleDateFormat("HH:mm")
    val date = Date(time)
    textView.text = format.format(date)
}

@BindingAdapter("timeYMD")
fun loadTimeYMD(textView: TextView, time: Long) {
    if (time == 0L) {
        textView.text = "- -"
    } else {
        val format = SimpleDateFormat("YYYY-MM-DD")
        val date = Date(time)
        textView.text = format.format(date)
    }

}

@BindingAdapter("imageUrl")
fun loadImage(imageView: ImageView, url: String?) {
    Glide.with(imageView.context).load(url)
        .placeholder(R.color.hintColor)
        .skipMemoryCache(false)
        .centerCrop()
        .into(imageView)
}

fun Double.toMoneyString(): String =
    NumberFormat.getInstance().apply {
        maximumFractionDigits = 2
        roundingMode = RoundingMode.HALF_UP
        isGroupingUsed = true
    }.format(this)

fun Context.dp2px(value: Float): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        value,
        resources.displayMetrics
    ).toInt()
}
