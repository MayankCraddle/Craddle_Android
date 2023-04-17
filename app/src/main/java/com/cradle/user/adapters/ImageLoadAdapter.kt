package com.cradle.user.adapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.cradle.utils.MyConstants

@BindingAdapter("imageFromUrl")
fun ImageView.imageFromUrl(url:String) {
    Glide.with(this.context).load(MyConstants.file_Base_URL+url).into(this)
}