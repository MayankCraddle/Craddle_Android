package com.cradle.model.cart

import com.google.gson.annotations.SerializedName

data class UserCartItem(

	@field:SerializedName("metaData")
	val metaData: MetaData? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("cartId")
	val cartId: String? = null,

	@field:SerializedName("state")
	val state: String? = null,
	@field:SerializedName("cartSize")
	val cartSize: String? = null,

	@field:SerializedName("userId")
	val userId: String? = null
)

data class ItemsItem(

	@field:SerializedName("country")
	val country: String? = null,
	@field:SerializedName("noOfPieces")
	val noOfPieces: String? = null,

	@field:SerializedName("quantity")
	var quantity: Int? = null,

	@field:SerializedName("productId")
	val productId: String? = null,

	@field:SerializedName("actualPrice")
	val actualPrice: Double? = null,

	@field:SerializedName("totalPrice")
	val totalPrice: Double? = null,

	@field:SerializedName("rating")
	val rating: Double? = null,

	@field:SerializedName("vendorId")
	val vendorId: String? = null,

	@field:SerializedName("itemId")
	val itemId: String? = null,

	@field:SerializedName("metaData")
	val metaData: MetaData? = null,

	@field:SerializedName("discountedPrice")
	val discountedPrice: Double? = null,

	@field:SerializedName("vendorEncryptedId")
	val vendorEncryptedId: String? = null,

	@field:SerializedName("variantMetadata")
	val variantMetadata: VariantMetadata? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("variant")
	val variant: String? = null,

	@field:SerializedName("currency")
	val currency: String? = null,


	@field:SerializedName("stock")
	val stock: Int? = null,

	@field:SerializedName("discountValue")
	val discountValue: Double? = null
)

data class MetaData(

	@field:SerializedName("items")
	val items: List<ItemsItem?>? = null,


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
	val productLength: Double? = null,

	@field:SerializedName("packageType")
	val packageType: Int? = null,

	@field:SerializedName("boxId")
	val boxId: Int? = null
)

