package com.cradle.model

import com.google.gson.annotations.SerializedName

data class SearchVendorResponse(

	@field:SerializedName("totalSize")
	val totalSize: Int? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("vendorList")
	val vendorList: List<SearchVendorVendorListItem?>? = null,

	@field:SerializedName("productList")
	val productList: List<SearchProductProductListItem?>? = null
)

data class SearchVendorDocumentsItem(

	@field:SerializedName("documentType")
	val documentType: String? = null,

	@field:SerializedName("document")
	val document: String? = null,

	@field:SerializedName("type")
	val type: Int? = null
)

data class SearchVendorSourcingItem(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: String? = null
)

data class SearchVendorRatingDetail(

	@field:SerializedName("1")
	val jsonMember1: Int? = null,

	@field:SerializedName("2")
	val jsonMember2: Int? = null,

	@field:SerializedName("3")
	val jsonMember3: Int? = null,

	@field:SerializedName("4")
	val jsonMember4: Int? = null,

	@field:SerializedName("5")
	val jsonMember5: Int? = null,

	@field:SerializedName("Total")
	val total: Int? = null
)

data class SearchVendorSocialMediaLinksItem(

	@field:SerializedName("link")
	val link: String? = null,

	@field:SerializedName("type")
	val type: String? = null
)

data class SearchVendorVendorListItem(

	@field:SerializedName("ratingDetail")
	val ratingDetail: SearchVendorRatingDetail? = null,

	@field:SerializedName("country")
	val country: String? = null,

	@field:SerializedName("emailMobile")
	val emailMobile: String? = null,

	@field:SerializedName("mobile")
	val mobile: String? = null,

	@field:SerializedName("rating")
	val rating: Float? = null,

	@field:SerializedName("vendorId")
	val vendorId: String? = null,

	@field:SerializedName("createdOn")
	val createdOn: String? = null,

	@field:SerializedName("manufacturer")
	val manufacturer: Boolean? = null,

	@field:SerializedName("metaData")
	val metaData: SearchVendorMetaData? = null,

	@field:SerializedName("emailVerified")
	val emailVerified: Boolean? = null,

	@field:SerializedName("approved")
	val approved: Boolean? = null,

	@field:SerializedName("exporter")
	val exporter: Boolean? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("wholesaler")
	val wholesaler: Boolean? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class SearchVendorMetaData(

	@field:SerializedName("lastName")
	val lastName: String? = null,

	@field:SerializedName("image")
	val image: Any? = null,

	@field:SerializedName("country")
	val country: Any? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("documents")
	val documents: List<SearchVendorDocumentsItem?>? = null,

	@field:SerializedName("companyName")
	val companyName: String? = null,

	@field:SerializedName("about")
	val about: Any? = null,

	@field:SerializedName("sourcing")
	val sourcing: List<Any?>? = null,

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
