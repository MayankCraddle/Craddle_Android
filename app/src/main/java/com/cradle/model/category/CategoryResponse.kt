package com.cradle.model.category

import com.google.gson.annotations.SerializedName

data class CategoryResponse(

	@field:SerializedName("totalSize")
	val totalSize: Int? = null,

	@field:SerializedName("parentCategoryList")
	val parentCategoryList: Any? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("categoryList")
	val categoryList: List<CategoryItem?>? = null
)

data class CategoryItem(

	@field:SerializedName("parentCategoryName")
	val parentCategoryName: String? = null,

	@field:SerializedName("addedBy")
	val addedBy: Any? = null,

	@field:SerializedName("createdOn")
	val createdOn: String? = null,

	@field:SerializedName("subCategories")
	val subCategories: List<SubCategoriesItem?>? = null,

	@field:SerializedName("tags")
	val tags: List<String?>? = null,

	@field:SerializedName("metaData")
	val metaData: MetaData? = null,

	@field:SerializedName("isSubCategoryAvailable")
	val isSubCategoryAvailable: Boolean? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("mainCategory")
	val mainCategory: Any? = null,

	@field:SerializedName("parentCategory")
	val parentCategory: String? = null,

	@field:SerializedName("comment")
	val comment: Any? = null,

	@field:SerializedName("id")
	val id: String? = null,


	@field:SerializedName("status")
	val status: String? = null
)

data class MetaData(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("mobileImage")
	val mobileImage: String? = null,

	@field:SerializedName("description")
	val description: String? = null
)

data class SubCategoriesItem(

	@field:SerializedName("parentCategoryName")
	val parentCategoryName: String? = null,

	@field:SerializedName("addedBy")
	val addedBy: Any? = null,

	@field:SerializedName("createdOn")
	val createdOn: Any? = null,

	@field:SerializedName("subCategories")
	val subCategories: List<SubSubCategoriesItem?>? = null,

	@field:SerializedName("tags")
	val tags: List<String?>? = null,

	@field:SerializedName("metaData")
	val metaData: MetaData? = null,

	@field:SerializedName("isSubCategoryAvailable")
	val isSubCategoryAvailable: Boolean? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("mainCategory")
	val mainCategory: Any? = null,

	@field:SerializedName("parentCategory")
	val parentCategory: String? = null,

	@field:SerializedName("comment")
	val comment: Any? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
data class SubSubCategoriesItem(

	@field:SerializedName("parentCategoryName")
	val parentCategoryName: String? = null,

	@field:SerializedName("addedBy")
	val addedBy: Any? = null,

	@field:SerializedName("createdOn")
	val createdOn: Any? = null,

	@field:SerializedName("subCategories")
	val subCategories: Any? = null,

	@field:SerializedName("tags")
	val tags: List<String?>? = null,

	@field:SerializedName("metaData")
	val metaData: MetaData? = null,

	@field:SerializedName("isSubCategoryAvailable")
	val isSubCategoryAvailable: Boolean? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("mainCategory")
	val mainCategory: Any? = null,

	@field:SerializedName("parentCategory")
	val parentCategory: String? = null,

	@field:SerializedName("comment")
	val comment: Any? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
