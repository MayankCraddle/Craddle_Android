package com.cradle.model

import com.google.gson.annotations.SerializedName

data class UserVenorListResponse(

	@field:SerializedName("searchText")
	val searchText: Any? = null,

	@field:SerializedName("totalSize")
	val totalSize: Any? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("vendorList")
	val vendorList: List<VendorListItem?>? = null
)

data class VendorListItem(

	@field:SerializedName("ratingDetail")
	val ratingDetail: RatingDetail? = null,

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
	val metaData: UserVenorListMetaData? = null,

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

data class SourcingItem(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: String? = null
)

data class RatingDetail(

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

data class DocumentsItem(

	@field:SerializedName("documentType")
	val documentType: String? = null,

	@field:SerializedName("document")
	val document: String? = null,

	@field:SerializedName("type")
	val type: Int? = null
)

data class UserVenorSocialMediaLinksItem(

	@field:SerializedName("link")
	val link: String? = null,

	@field:SerializedName("type")
	val type: String? = null
)

data class UserVenorListMetaData(

	@field:SerializedName("lastName")
	val lastName: String? = null,

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("country")
	val country: String? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("documents")
	val documents: List<DocumentsItem?>? = null,

	@field:SerializedName("companyName")
	val companyName: String? = null,

	@field:SerializedName("about")
	val about: String? = null,

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
	val coverImage: String? = null,

	@field:SerializedName("socialMediaLinks")
	val socialMediaLinks: List<UserVenorSocialMediaLinksItem?>? = null,

	@field:SerializedName("returns")
	val returns: Any? = null,

	@field:SerializedName("state")
	val state: String? = null,

	@field:SerializedName("landmark")
	val landmark: String? = null
)
