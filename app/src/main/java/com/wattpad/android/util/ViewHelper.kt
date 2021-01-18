package com.wattpad.android.util

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.wattpad.android.R

/**
 * This class is for providing extension functions for views
 */

object ViewHelper {

    fun ImageView.loadImage(imageUrl: String) {
        Glide.with(this.context)
            .load(imageUrl)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .placeholder(R.drawable.wattpad)
            .into(this)
    }
}