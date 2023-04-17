package com.cradle.model

import com.google.gson.annotations.SerializedName

data class UserVendorRatingResponse(

	@field:SerializedName("ratingList")
	val ratingList: List<RatingListItem?>? = null,

	@field:SerializedName("success")
	val success: Boolean? = null
)

data class UserVendorRatingMetaData(

	@field:SerializedName("firstName")
	val firstName: String? = null,

	@field:SerializedName("lastName")
	val lastName: String? = null,

	@field:SerializedName("image")
	val image: Any? = null,

	@field:SerializedName("coverImage")
	val coverImage: Any? = null
)

data class RatingListItem(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("userMetaData")
	val userMetaData: UserVendorRatingMetaData? = null,

	@field:SerializedName("review")
	val review: String? = null,

	@field:SerializedName("rating")
	val rating: Float? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("fromId")
	val fromId: String? = null
)
