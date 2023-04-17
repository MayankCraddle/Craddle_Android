package com.cradle.model

import com.cradle.model.trade.VariantsItem
import com.google.gson.annotations.SerializedName

data class OnSaleResponse(

	@field:SerializedName("totalSize")
	val totalSize: Any? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("count")
	val count: Int? = null,

	@field:SerializedName("productList")
	val productList: List<ProductListItem?>? = null
)

data class OnSaleMetaData(

	@field:SerializedName("noOfPieces")
	val noOfPieces: Int? = null,

	@field:SerializedName("images")
	val images: List<String?>? = null,

	@field:SerializedName("material")
	val material: String? = null,

	@field:SerializedName("productWidth")
	val productWidth: Double? = null,

	@field:SerializedName("productHeight")
	val productHeight: Double? = null,

	@field:SerializedName("dimensionUnit")
	val dimensionUnit: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("shortDescription")
	val shortDescription: String? = null,

	@field:SerializedName("productLength")
	val productLength: Double? = null
)

data class ProductListItem(

	@field:SerializedName("productId")
	val productId: String? = null,

	@field:SerializedName("actualPrice")
	val actualPrice: Double? = null,

	@field:SerializedName("rating")
	val rating: Double? = null,

	@field:SerializedName("vendorId")
	val vendorId: Any? = null,

	@field:SerializedName("wishlisted")
	val wishlisted: Boolean? = null,

	@field:SerializedName("vendorName")
	val vendorName: Any? = null,

	@field:SerializedName("categoryName")
	val categoryName: String? = null,

	@field:SerializedName("tags")
	val tags: List<Any?>? = null,

	@field:SerializedName("itemId")
	val itemId: String? = null,

	@field:SerializedName("metaData")
	val metaData: OnSaleMetaData? = null,

	@field:SerializedName("discountedPrice")
	val discountedPrice: Double? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("currency")
	val currency: String? = null,

	@field:SerializedName("discountType")
	val discountType: String? = null,

	@field:SerializedName("stock")
	val stock: Int? = null,

	@field:SerializedName("dimension")
	val dimension: String? = null,

	@field:SerializedName("discountValue")
	val discountValue: Double? = null,

	@field:SerializedName("orderable")
	val orderable: String? = null,

	@field:SerializedName("variants")
	val variants: List<VariantsItem?>? = null,

	@field:SerializedName("categoryId")
	val categoryId: String? = null

)
