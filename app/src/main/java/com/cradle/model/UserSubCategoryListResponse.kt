package com.cradle.model

import com.google.gson.annotations.SerializedName

data class UserSubCategoryListResponse(

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("categoryList")
	val categoryList: List<UserSubCategoryListItem?>? = null
)

data class UserSubCategoryListItem(

	@field:SerializedName("metaData")
	val metaData: UserSubCategoryListMetaData? = null,

	@field:SerializedName("isSubCategoryAvailable")
	val isSubCategoryAvailable: Boolean? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("parentCategory")
	val parentCategory: String? = null,

	@field:SerializedName("comment")
	val comment: Any? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class UserSubCategoryListMetaData(

	@field:SerializedName("image")
	val image: Any? = null,

	@field:SerializedName("description")
	val description: String? = null
)
