package com.cradle.model

import com.google.gson.annotations.SerializedName


data class ProductDetailsResponse(

	@field:SerializedName("country")
	val country: String? = null,

	@field:SerializedName("actualPrice")
	val actualPrice: String? = null,

	@field:SerializedName("emailMobile")
	val emailMobile: String? = null,
	@field:SerializedName("shareUrl")
	val shareUrl: String? = null,

	@field:SerializedName("rating")
	val rating: Float? = null,

	@field:SerializedName("wishlisted")
	val wishlisted: Boolean? = null,

	@field:SerializedName("vendorId")
	val vendorId: String? = null,

	@field:SerializedName("categoryName")
	val categoryName: Any? = null,
	@field:SerializedName("vendorEncryptedId")
	val vendorEncryptedId: String? = null,

	@field:SerializedName("fcmKey")
	val fcmKey: String? = null,

	@field:SerializedName("vendorRating")
	val vendorRating: String? = null,

	@field:SerializedName("metaData")
	val metaData: ProductDetailsMetaData? = null,

	@field:SerializedName("vendorMetaData")
	val vendorMetaData: VendorMetaData? = null,

	@field:SerializedName("onSale")
	val onSale: Boolean? = null,

	@field:SerializedName("discountType")
	val discountType: String? = null,

	@field:SerializedName("currency")
	val currency: String? = null,

	@field:SerializedName("stock")
	val stock: Int? = null,

	@field:SerializedName("dimension")
	val dimension: String? = null,

	@field:SerializedName("productId")
	val productId: String? = null,

	@field:SerializedName("returnPolicy")
	val returnPolicy: String? = null,

	@field:SerializedName("vendorName")
	val vendorName: String? = null,

	@field:SerializedName("tags")
	val tags: List<Any?>? = null,

	@field:SerializedName("itemId")
	val itemId: String? = null,

	@field:SerializedName("discountedPrice")
	val discountedPrice: Int? = null,

	@field:SerializedName("productRatingDetail")
	val productRatingDetail: ProductRatingDetail? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("chatResponsePercentage")
	val chatResponsePercentage: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("shipOnTimePercentage")
	val shipOnTimePercentage: String? = null,

	@field:SerializedName("discountValue")
	val discountValue: String? = null,

	@field:SerializedName("variants")
	val variants: List<VariantsItem?>? = null,

	@field:SerializedName("categoryId")
	val categoryId: String? = null,
	@field:SerializedName("orderable")
	val orderable: String? = null
)

data class ProductDetailsMetaData(

	@field:SerializedName("noOfPieces")
	val noOfPieces: String? = null,

	@field:SerializedName("images")
	val images: List<String?>? = null,

	@field:SerializedName("material")
	val material: String? = null,

	@field:SerializedName("productWidth")
	val productWidth: Float? = null,

	@field:SerializedName("productWeight")
	val productWeight: String? = null,

	@field:SerializedName("productHeight")
	val productHeight: Float? = null,

	@field:SerializedName("dimensionUnit")
	val dimensionUnit: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("shortDescription")
	val shortDescription: String? = null,

	@field:SerializedName("productLength")
	val productLength: Float? = null
)

data class ProductRatingDetail(

	@field:SerializedName("1")
	val jsonMember1: Float? = null,

	@field:SerializedName("2")
	val jsonMember2: Float? = null,

	@field:SerializedName("3")
	val jsonMember3: Float? = null,

	@field:SerializedName("4")
	val jsonMember4: Float? = null,

	@field:SerializedName("5")
	val jsonMember5: Float? = null,

	@field:SerializedName("Total")
	val total: Float? = null
)

data class SocialMediaLinksItem(

	@field:SerializedName("link")
	var link: String? = null,

	@field:SerializedName("type")
	val type: String? = null
)

data class VendorMetaData(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("country")
	val country: String? = null,

	@field:SerializedName("lastName")
	val lastName: String? = null,

	@field:SerializedName("documents")
	val documents: Any? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("companyName")
	val companyName: String? = null,

	@field:SerializedName("about")
	val about: String? = null,

	@field:SerializedName("sourcing")
	val sourcing: List<Any?>? = null,

	@field:SerializedName("zipcode")
	val zipcode: String? = null,

	@field:SerializedName("firstName")
	val firstName: String? = null,

	@field:SerializedName("streetAddress")
	val streetAddress: String? = null,

	@field:SerializedName("coverImage")
	val coverImage: String? = null,

	@field:SerializedName("socialMediaLinks")
	val socialMediaLinks: List<SocialMediaLinksItem?>? = null,

	@field:SerializedName("state")
	val state: String? = null,

	@field:SerializedName("landmark")
	val landmark: String? = null
)
data class VariantsItem(

	@field:SerializedName("itemId")
	val itemId: String? = null,

	@field:SerializedName("discountedPrice")
	val discountedPrice: Int? = null,

	@field:SerializedName("actualPrice")
	val actualPrice: Int? = null,

	@field:SerializedName("price")
	val price: String? = null,
	@field:SerializedName("minQuantity")
	val minQuantity: Int? = null,

	@field:SerializedName("variant")
	val variant: String? = null,

	@field:SerializedName("currency")
	val currency: String? = null,

	@field:SerializedName("discountType")
	val discountType: String? = null,

	@field:SerializedName("stock")
	val stock: Int? = null,

	@field:SerializedName("discountValue")
	val discountValue: String? = null
)
