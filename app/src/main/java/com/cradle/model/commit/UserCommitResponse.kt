package com.cradle.model.commit

import com.google.gson.annotations.SerializedName

data class UserCommitResponse(

	@field:SerializedName("comments")
	val comments: List<CommentsItem?>? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("count")
	val count: Int? = null
)

data class CommentsItem(

	@field:SerializedName("image")
	var image: Any? = null,

	@field:SerializedName("createdBy")
	var createdBy: String? = null,

	@field:SerializedName("comment")
	var comment: String? = null,

	@field:SerializedName("createdOn")
	var createdOn: String? = null
)
