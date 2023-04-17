package com.cradle.model.media

import com.google.gson.annotations.SerializedName

data class CategoryList(
    @field:SerializedName("catlistItem")
    var catlistItem: List<CatListItem>? = mutableListOf()
)

data class CatListItem(
    @field:SerializedName("categoryName") var categoryName: String? = null,
    @field:SerializedName("icon") var icon: String? = null,
    @field:SerializedName("priority") var priority: Int? = null,
    @field:SerializedName("pos") var pos: Int? = null
)