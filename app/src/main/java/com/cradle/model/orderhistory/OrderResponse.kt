package com.cradle.model.orderhistory

import com.google.gson.annotations.SerializedName

data class OrderResponse(

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("orderList")
	val orderList: List<OrderListItem?>? = null
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

data class MetaData(

	@field:SerializedName("noOfPieces")
	val noOfPieces: Int? = null,

	@field:SerializedName("images")
	val images: List<String?>? = null,

	@field:SerializedName("material")
	val material: String? = null,

	@field:SerializedName("productWidth")
	val productWidth: String? = null,

	@field:SerializedName("productHeight")
	val productHeight: String? = null,

	@field:SerializedName("dimensionUnit")
	val dimensionUnit: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("shortDescription")
	val shortDescription: String? = null,

	@field:SerializedName("productLength")
	val productLength: String? = null
)

data class OrderListItem(

	@field:SerializedName("billingAddressMetaData")
	val billingAddressMetaData: BillingAddressMetaData? = null,

	@field:SerializedName("orderId")
	val orderId: String? = null,

	@field:SerializedName("cartMetaData")
	val cartMetaData: CartMetaData? = null,

	@field:SerializedName("totalPrice")
	val totalPrice: Float? = null,

	@field:SerializedName("isRated")
	val isRated: Boolean? = null,

	@field:SerializedName("vendorMetadata")
	val vendorMetadata: VendorMetadata? = null,

	@field:SerializedName("rating")
	val rating: Float? = null,

	@field:SerializedName("discount")
	val discount: Any? = null,

	@field:SerializedName("vendorId")
	val vendorId: Any? = null,

	@field:SerializedName("subTotal")
	val subTotal: Any? = null,

	@field:SerializedName("createdOn")
	val createdOn: String? = null,

	@field:SerializedName("orderState")
	val orderState: String? = null,

	@field:SerializedName("parentOrderId")
	val parentOrderId: String? = null,

	@field:SerializedName("shippingRate")
	val shippingRate: String? = null,

	@field:SerializedName("review")
	val review: Any? = null,

	@field:SerializedName("addressMetaData")
	val addressMetaData: AddressMetaData? = null,

	@field:SerializedName("orderBy")
	val orderBy: String? = null
)

data class CartMetaData(

	@field:SerializedName("items")
	val items: List<ItemsItem?>? = null
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

data class ItemsItem(

	@field:SerializedName("country")
	val country: Any? = null,

	@field:SerializedName("quantity")
	val quantity: Int? = null,

	@field:SerializedName("productId")
	val productId: String? = null,

	@field:SerializedName("actualPrice")
	val actualPrice: Float? = null,

	@field:SerializedName("totalPrice")
	val totalPrice: Float? = null,

	@field:SerializedName("rating")
	val rating: Float? = null,

	@field:SerializedName("itemId")
	val itemId: String? = null,

	@field:SerializedName("metaData")
	val metaData: MetaData? = null,

	@field:SerializedName("discountedPrice")
	val discountedPrice: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("currency")
	val currency: String? = null,

	@field:SerializedName("stock")
	val stock: String? = null,

	@field:SerializedName("discountValue")
	val discountValue: String? = null
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
	val about: String? = null,

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
	val coverImage: String? = null,

	@field:SerializedName("socialMediaLinks")
	val socialMediaLinks: Any? = null,

	@field:SerializedName("returns")
	val returns: Any? = null,

	@field:SerializedName("state")
	val state: String? = null,

	@field:SerializedName("landmark")
	val landmark: String? = null
)
