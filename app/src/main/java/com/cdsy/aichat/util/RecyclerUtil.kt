package com.cdsy.aichat.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.addBottomSpace(spaceDp: Int = 100) {
    val spacePx = (spaceDp * resources.displayMetrics.density).toInt()
    addItemDecoration(object : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val layoutManager = parent.layoutManager as? GridLayoutManager ?: return
            val position = parent.getChildAdapterPosition(view)
            val spanCount = layoutManager.spanCount
            val itemCount = parent.adapter?.itemCount ?: 0

            // 计算当前item所在的行（考虑spanSizeLookup）
            var currentRow = 0
            var currentRowSpan = 0
            for (i in 0 until position) {
                val spanSize = layoutManager.spanSizeLookup.getSpanSize(i)
                currentRowSpan += spanSize
                if (currentRowSpan >= spanCount) {
                    currentRow++
                    currentRowSpan = 0
                }
            }

            // 计算总行数
            var totalRowSpan = 0
            var totalRows = 0
            for (i in 0 until itemCount) {
                val spanSize = layoutManager.spanSizeLookup.getSpanSize(i)
                totalRowSpan += spanSize
                if (totalRowSpan >= spanCount) {
                    totalRows++
                    totalRowSpan = 0
                }
            }
            if (totalRowSpan > 0) {
                totalRows++
            }

            // 如果当前item在最后一行，添加底部间隙
            if (currentRow == totalRows - 1) {
                outRect.bottom = spacePx
            }
        }
    })
}