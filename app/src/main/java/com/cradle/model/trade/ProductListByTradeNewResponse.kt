package com.cradle.model.trade

import com.google.gson.annotations.SerializedName

data class ProductListByTradeNewResponse(

	@field:SerializedName("totalSize")
	val totalSize: Any? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("count")
	val count: Int? = null,

	@field:SerializedName("productList")
	val productList: List<ProductListItem?>? = null
)

data class MetaData(

	@field:SerializedName("images")
	val images: List<String?>? = null,

	@field:SerializedName("productHeight")
	val productHeight: Int? = null,

	@field:SerializedName("dimensionUnit")
	val dimensionUnit: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("shortDescription")
	val shortDescription: String? = null,

	@field:SerializedName("productWeight")
	val productWeight: Int? = null,

	@field:SerializedName("packageType")
	val packageType: Int? = null,

	@field:SerializedName("noOfPieces")
	val noOfPieces: Int? = null,

	@field:SerializedName("material")
	val material: String? = null,

	@field:SerializedName("productWidth")
	val productWidth: Int? = null,

	@field:SerializedName("productLength")
	val productLength: Int? = null,

	@field:SerializedName("weightUnit")
	val weightUnit: String? = null,

	@field:SerializedName("boxId")
	val boxId: Int? = null
)

data class ProductListItem(

	@field:SerializedName("country")
	val country: String? = null,

	@field:SerializedName("actualPrice")
	val actualPrice: Int? = null,

	@field:SerializedName("orderable")
	val orderable: Boolean? = null,

	@field:SerializedName("rating")
	val rating: Int? = null,

	@field:SerializedName("vendorId")
	val vendorId: String? = null,

	@field:SerializedName("wishlisted")
	val wishlisted: Boolean? = null,

	@field:SerializedName("variants")
	val variants: List<VariantsItem?>? = null,

	@field:SerializedName("categoryName")
	val categoryName: String? = null,

	@field:SerializedName("metaData")
	val metaData: MetaData? = null,

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

	@field:SerializedName("productId")
	val productId: String? = null,

	@field:SerializedName("active")
	val active: Boolean? = null,

	@field:SerializedName("slugName")
	val slugName: String? = null,

	@field:SerializedName("vendorName")
	val vendorName: String? = null,

	@field:SerializedName("tags")
	val tags: List<String?>? = null,

	@field:SerializedName("itemId")
	val itemId: String? = null,

	@field:SerializedName("discountedPrice")
	val discountedPrice: Int? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("vendorEncryptedId")
	val vendorEncryptedId: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("sampleAvailable")
	val sampleAvailable: Boolean? = null,

	@field:SerializedName("discountValue")
	val discountValue: Int? = null,

	@field:SerializedName("categoryId")
	val categoryId: String? = null
)

data class VariantsItem(

	@field:SerializedName("itemId")
	val itemId: String? = null,

	@field:SerializedName("discountedPrice")
	val discountedPrice: Int? = null,

	@field:SerializedName("minQuantity")
	val minQuantity: Int? = null,

	@field:SerializedName("actualPrice")
	val actualPrice: Int? = null,

	@field:SerializedName("price")
	val price: Int? = null,

	@field:SerializedName("variant")
	val variant: String? = null,

	@field:SerializedName("currency")
	val currency: String? = null,

	@field:SerializedName("discountType")
	val discountType: String? = null,

	@field:SerializedName("stock")
	val stock: Int? = null,

	@field:SerializedName("discountValue")
	val discountValue: Int? = null
)
