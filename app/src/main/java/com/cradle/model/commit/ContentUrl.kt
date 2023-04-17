package com.cradle.model.commit

import com.google.gson.annotations.SerializedName

data class ContentUrl(

    @field:SerializedName("img")
    val img: String? = null,

    @field:SerializedName("url")
    val youtubeurl: String? = null,

)