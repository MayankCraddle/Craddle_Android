package com.cradle.model

import com.cradle.model.trade.VariantsItem
import com.cradle.model.wishlist.VariantMetadata
import com.google.gson.annotations.SerializedName

data class UserWishListResponse(

	@field:SerializedName("metaData")
	val metaData: MetaData? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("wishlistId")
	val wishlistId: String? = null,

	@field:SerializedName("userId")
	val userId: String? = null
)

data class UserWishList(

	@field:SerializedName("itemId")
	val itemId: String? = null,

	@field:SerializedName("metaData")
	val metaData: MetaData? = null,

	@field:SerializedName("quantity")
	val quantity: String? = null,

	@field:SerializedName("discountValue")
	val discountPercent: String? = null,

	@field:SerializedName("productId")
	val productId: String? = null,

	@field:SerializedName("discountedPrice")
	val discountedPrice: String? = null,

	@field:SerializedName("totalPrice")
	val totalPrice: String? = null,

	@field:SerializedName("price")
	val price: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("rating")
	val rating: String? = null,

	@field:SerializedName("currency")
	val currency: String? = null,

	@field:SerializedName("orderable")
	val orderable: String? = null,
	@field:SerializedName("variantMetadata")
	val variantMetadata: VariantMetadata? = null,
	@field:SerializedName("minQuantity")
	val minQuantity: Int? = null,

	@field:SerializedName("stock")
	val stock: Int? = null
)

data class MetaData(

	@field:SerializedName("items")
	val items: List<UserWishList?>? = null,

	@field:SerializedName("images")
	val images: List<String?>? = null,

	@field:SerializedName("description")
	val description: String? = null
)
