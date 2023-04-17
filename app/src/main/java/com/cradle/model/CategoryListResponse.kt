package com.cradle.model

import com.google.gson.annotations.SerializedName

data class CategoryListResponse(

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("categoryList")
	val categoryList: List<CategoryListItem?>? = null
)

data class CategoryLisMetaData(

	@field:SerializedName("image")
	val image: Any? = null,

	@field:SerializedName("description")
	val description: String? = null
)

data class CategoryListItem(

	@field:SerializedName("metaData")
	val metaData: CategoryLisMetaData? = null,

	@field:SerializedName("isSubCategoryAvailable")
	val isSubCategoryAvailable: Boolean? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("parentCategory")
	val parentCategory: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
