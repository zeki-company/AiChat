package com.cdsy.aichat.ui.splash

import android.widget.ImageView
import com.cdsy.aichat.R
import com.cdsy.aichat.util.loadImage
import com.zhpan.bannerview.BaseBannerAdapter
import com.zhpan.bannerview.BaseViewHolder

/**
 * @author Zeki(https://github.com/ZekiNN)
 * @date 2025/7/11
 */
class BannerAdapter(val showCover: Boolean) : BaseBannerAdapter<String>() {
    override fun bindData(
        holder: BaseViewHolder<String>,
        data: String,
        position: Int,
        pageSize: Int
    ) {
        if(!showCover){
            holder.findViewById<ImageView>(R.id.banner_image).foreground = null
        }

        loadImage(holder.findViewById(R.id.banner_image), data)
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_corner_image
    }

}

