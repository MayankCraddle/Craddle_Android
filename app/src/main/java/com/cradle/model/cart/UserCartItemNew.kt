package com.cradle.model.cart

import com.google.gson.annotations.SerializedName

data class UserCartItemNew(

	@field:SerializedName("metaData")
	val metaData: MetaDataNew? = null,

	@field:SerializedName("cartSize")
	val cartSize: Int? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("cartId")
	val cartId: String? = null,

	@field:SerializedName("state")
	val state: String? = null,

	@field:SerializedName("userId")
	val userId: String? = null
)

data class MetaDataNew(

	@field:SerializedName("items")
	val items: List<ItemsItemNew?>? = null,

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

data class ItemsItemNew(

	@field:SerializedName("country")
	val country: String? = null,

	@field:SerializedName("quantity")
	val quantity: Int? = null,

	@field:SerializedName("productId")
	val productId: String? = null,

	@field:SerializedName("actualPrice")
	val actualPrice: Int? = null,

	@field:SerializedName("totalPrice")
	val totalPrice: Int? = null,

	@field:SerializedName("orderable")
	val orderable: Boolean? = null,

	@field:SerializedName("rating")
	val rating: Int? = null,

	@field:SerializedName("vendorId")
	val vendorId: String? = null,

	@field:SerializedName("slugName")
	val slugName: String? = null,

	@field:SerializedName("itemId")
	val itemId: String? = null,

	@field:SerializedName("metaData")
	val metaData: MetaData? = null,

	@field:SerializedName("discountedPrice")
	val discountedPrice: Int? = null,

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
	val discountValue: Int? = null
)

data class VariantMetadata(

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
	val discountType: Any? = null,

	@field:SerializedName("stock")
	val stock: Int? = null,

	@field:SerializedName("discountValue")
	val discountValue: Int? = null
)
