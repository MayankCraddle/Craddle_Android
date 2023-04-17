package com.cradle.model

import com.google.gson.annotations.SerializedName


data class SearchProductProductListItem(

	@field:SerializedName("country")
	val country: Any? = null,

	@field:SerializedName("productId")
	val productId: String? = null,

	@field:SerializedName("actualPrice")
	val actualPrice: Float? = null,

	@field:SerializedName("rating")
	val rating: Float? = null,

	@field:SerializedName("vendorId")
	val vendorId: String? = null,

	@field:SerializedName("wishlisted")
	val wishlisted: Boolean? = null,

	@field:SerializedName("vendorName")
	val vendorName: String? = null,

	@field:SerializedName("categoryName")
	val categoryName: String? = null,

	@field:SerializedName("tags")
	val tags: List<String?>? = null,

	@field:SerializedName("itemId")
	val itemId: String? = null,

	@field:SerializedName("metaData")
	val metaData: SearchProductMetaData? = null,

	@field:SerializedName("discountedPrice")
	val discountedPrice: Int? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("onSale")
	val onSale: Boolean? = null,

	@field:SerializedName("currency")
	val currency: String? = null,

	@field:SerializedName("discountType")
	val discountType: String? = null,

	@field:SerializedName("stock")
	val stock: Int? = null,

	@field:SerializedName("dimension")
	val dimension: String? = null,

	@field:SerializedName("discountValue")
	val discountValue: Float? = null,

	@field:SerializedName("categoryId")
	val categoryId: String? = null
)

data class SearchProductMetaData(

	@field:SerializedName("noOfPieces")
	val noOfPieces: Int? = null,

	@field:SerializedName("images")
	val images: List<String?>? = null,

	@field:SerializedName("material")
	val material: String? = null,

	@field:SerializedName("productWidth")
	val productWidth: Float? = null,

	@field:SerializedName("productHeight")
	val productHeight: Float? = null,

	@field:SerializedName("dimensionUnit")
	val dimensionUnit: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("shortDescription")
	val shortDescription: String? = null,

	@field:SerializedName("productLength")
	val productLength: String? = null
)
