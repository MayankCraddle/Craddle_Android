package com.cradle.model

import com.google.gson.annotations.SerializedName
import com.cradle.model.commit.CommentsItem

data class ViewAllResponse(

	@field:SerializedName("sectionName")
	val sectionName: String? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("sectionId")
	val sectionId: Int? = null,

	@field:SerializedName("contentList")
	val contentList: List<ContentListItem?>? = null
)

data class ContentListItem(

	@field:SerializedName("country")
	val country: String? = null,


	@field:SerializedName("featured")
	val featured: Boolean? = null,

	@field:SerializedName("subject")
	val subject: String? = null,

	@field:SerializedName("author")
	val author: Any? = null,

	@field:SerializedName("sectionId")
	val sectionId: Int? = null,

	@field:SerializedName("shortDescription")
	var shortDescription: String? = null,

	@field:SerializedName("body")
	val body: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("createdOn")
	val createdOn: String? = null,

	@field:SerializedName("sectionName")
	val sectionName: Any? = null,

	@field:SerializedName("metaData")
	val metaData: ViewAllMetaData? = null,

	@field:SerializedName("featuredImage")
	val featuredImage: String? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("files")
	val files: List<String?>? = null,

	@field:SerializedName("id")
	val id: Int? = null,
	@field:SerializedName("commentCount")
    var commentCount: Int? = null,
	@field:SerializedName("readCount")
	val readCount: Int? = null,
	@field:SerializedName("videoUrl")
	val videoUrl: String? = null,
	@field:SerializedName("priority")
    var priority: String? = null,
	@field:SerializedName("videoId")
	val videoId: String? = null,
	@field:SerializedName("comments")
	val comments: ArrayList<CommentsItem?>? = null,
)

data class ViewAllMetaData(

	@field:SerializedName("location")
	val location: String? = null,

	@field:SerializedName("redirectionUrl")
	val redirectionUrl: String? = null,

	@field:SerializedName("eventDate")
	val eventDate: String? = null
)
