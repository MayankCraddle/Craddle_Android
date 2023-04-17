package com.cradle.model

import com.google.gson.annotations.SerializedName

data class UserBlogDetailsResponse(

	@field:SerializedName("country")
	val country: String? = null,

	@field:SerializedName("featured")
	val featured: Boolean? = null,

	@field:SerializedName("subject")
	val subject: String? = null,

	@field:SerializedName("author")
	val author: String? = null,

	@field:SerializedName("sectionId")
	val sectionId: Int? = null,

	@field:SerializedName("shortDescription")
	val shortDescription: String? = null,

	@field:SerializedName("readCount")
	val readCount: Int? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("body")
	val body: String? = null,

	@field:SerializedName("createdOn")
	val createdOn: String? = null,

	@field:SerializedName("commentCount")
	val commentCount: Int? = null,

	@field:SerializedName("sectionName")
	val sectionName: String? = null,

	@field:SerializedName("metaData")
	val metaData: BlogDetailsMetaData? = null,

	@field:SerializedName("videoUrl")
	val videoUrl: String? = null,
	@field:SerializedName("link")
	val link: String? = null,

	@field:SerializedName("shareURl")
	val shareURl: String? = null,

	@field:SerializedName("videoId")
	val videoId: String? = null,

	@field:SerializedName("featuredImage")
	val featuredImage: String? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("files")
	val files: List<String?>? = null,

	@field:SerializedName("id")
	val id: Int? = null
)

data class BlogDetailsMetaData(

	@field:SerializedName("location")
	val location: String? = null,

	@field:SerializedName("redirectionUrl")
	val redirectionUrl: String? = null,


	@field:SerializedName("eventDate")
	val eventDate: String? = null
)
