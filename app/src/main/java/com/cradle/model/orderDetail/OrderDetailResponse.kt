package com.cradle.model.orderDetail

import com.google.gson.annotations.SerializedName

data class OrderDetailResponse(

	@field:SerializedName("billingAddressMetaData")
	val billingAddressMetaData: BillingAddressMetaData? = null,

	@field:SerializedName("orderId")
	val orderId: String? = null,

	@field:SerializedName("cartMetaData")
	val cartMetaData: CartMetaData? = null,

	@field:SerializedName("totalPrice")
	val totalPrice: String? = null,

	@field:SerializedName("isRated")
	val isRated: Any? = null,

	@field:SerializedName("vendorMetadata")
	val vendorMetadata: VendorMetadata? = null,

	@field:SerializedName("rating")
	val rating: String? = null,

	@field:SerializedName("discount")
	val discount: String? = null,

	@field:SerializedName("vendorId")
	val vendorId: String? = null,

	@field:SerializedName("orderBy")
	val orderBy: String? = null,

	@field:SerializedName("subTotal")
	val subTotal: String? = null,

	@field:SerializedName("createdOn")
	val createdOn: String? = null,

	@field:SerializedName("orderState")
	val orderState: String? = null,

	@field:SerializedName("shippingRate")
	val shippingRate: String? = null,

	@field:SerializedName("userImage")
	val userImage: String? = null,

	@field:SerializedName("parentOrderId")
	val parentOrderId: String? = null,

	@field:SerializedName("review")
	val review: Any? = null,

	@field:SerializedName("currency")
	val currency: String? = null,

	@field:SerializedName("addressMetaData")
	val addressMetaData: AddressMetaData? = null
)

data class AddressMetaData(

	@field:SerializedName("zipcode")
	val zipcode: String? = null,

	@field:SerializedName("firstName")
	val firstName: String? = null,

	@field:SerializedName("lastName")
	val lastName: String? = null,

	@field:SerializedName("country")
	val country: String? = null,

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("streetAddress")
	val streetAddress: String? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("addressType")
	val addressType: String? = null,

	@field:SerializedName("phoneCode")
	val phoneCode: String? = null,

	@field:SerializedName("state")
	val state: String? = null,

	@field:SerializedName("landmark")
	val landmark: String? = null
)

data class CartMetaData(

	@field:SerializedName("items")
	val items: List<ItemsItem?>? = null
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
	val productWeight: String? = null,

	@field:SerializedName("packageType")
	val packageType: String? = null,

	@field:SerializedName("noOfPieces")
	val noOfPieces: String? = null,

	@field:SerializedName("material")
	val material: String? = null,

	@field:SerializedName("productWidth")
	val productWidth: String? = null,

	@field:SerializedName("productLength")
	val productLength: String? = null,

	@field:SerializedName("weightUnit")
	val weightUnit: String? = null,

	@field:SerializedName("boxId")
	val boxId: String? = null
)

data class VendorMetadata(

	@field:SerializedName("lastName")
	val lastName: String? = null,

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("country")
	val country: String? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("documents")
	val documents: Any? = null,

	@field:SerializedName("companyName")
	val companyName: String? = null,

	@field:SerializedName("about")
	val about: Any? = null,

	@field:SerializedName("sourcing")
	val sourcing: Any? = null,

	@field:SerializedName("refunds")
	val refunds: Any? = null,

	@field:SerializedName("zipcode")
	val zipcode: String? = null,

	@field:SerializedName("firstName")
	val firstName: String? = null,

	@field:SerializedName("shipping")
	val shipping: Any? = null,

	@field:SerializedName("streetAddress")
	val streetAddress: String? = null,

	@field:SerializedName("coverImage")
	val coverImage: Any? = null,

	@field:SerializedName("socialMediaLinks")
	val socialMediaLinks: Any? = null,

	@field:SerializedName("returns")
	val returns: Any? = null,

	@field:SerializedName("state")
	val state: String? = null,

	@field:SerializedName("landmark")
	val landmark: String? = null
)

data class ItemsItem(

	@field:SerializedName("country")
	val country: String? = null,

	@field:SerializedName("quantity")
	val quantity: String? = null,

	@field:SerializedName("productId")
	val productId: String? = null,

	@field:SerializedName("actualPrice")
	val actualPrice: String? = null,

	@field:SerializedName("totalPrice")
	val totalPrice: String? = null,

	@field:SerializedName("rating")
	val rating: String? = null,

	@field:SerializedName("vendorId")
	val vendorId: String? = null,

	@field:SerializedName("itemId")
	val itemId: String? = null,

	@field:SerializedName("metaData")
	val metaData: MetaData? = null,

	@field:SerializedName("discountedPrice")
	val discountedPrice: Int? = null,

	@field:SerializedName("vendorEncryptedId")
	val vendorEncryptedId: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("currency")
	val currency: String? = null,

	@field:SerializedName("stock")
	val stock: Int? = null,

	@field:SerializedName("discountValue")
	val discountValue: Int? = null
)

data class BillingAddressMetaData(

	@field:SerializedName("zipcode")
	val zipcode: String? = null,

	@field:SerializedName("firstName")
	val firstName: String? = null,

	@field:SerializedName("lastName")
	val lastName: String? = null,

	@field:SerializedName("country")
	val country: String? = null,

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("streetAddress")
	val streetAddress: String? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("addressType")
	val addressType: String? = null,

	@field:SerializedName("phoneCode")
	val phoneCode: String? = null,

	@field:SerializedName("state")
	val state: String? = null,

	@field:SerializedName("landmark")
	val landmark: String? = null
)
