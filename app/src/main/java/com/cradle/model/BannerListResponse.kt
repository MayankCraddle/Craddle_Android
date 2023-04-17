package com.cradle.model

import com.google.gson.annotations.SerializedName

data class BannerListResponse(
    @field:SerializedName("bannerList")
    val bannerList: List<String?>? = null,

    @field:SerializedName("success")
    val success: Boolean? = null
)
