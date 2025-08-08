package com.cdsy.aichat.ui.base

import androidx.recyclerview.widget.RecyclerView

/**
 * @Author: ZEKI https://github.com/ZYF99
 * @Date: 2021/6/7 6:16 下午
 */
fun RecyclerView.attachLoadMore(loadMoreAction:()->Unit){
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if (!recyclerView.canScrollVertically(1)) loadMoreAction()
        }
    })
}