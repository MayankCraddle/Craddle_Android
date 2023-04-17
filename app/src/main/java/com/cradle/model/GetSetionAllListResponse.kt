package com.cradle.model

import com.google.gson.annotations.SerializedName

data class GetSetionAllListResponse(

	@field:SerializedName("totalSize")
	val totalSize: Int? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("sections")
	val sections: ArrayList<SectionsItem?>? = null
)

data class SectionsItem(

	@field:SerializedName("sectionName")
	val sectionName: String? = null,

	@field:SerializedName("designSchema")
	val designSchema: String? = null,
	@field:SerializedName("icon")
	val icon: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("priority")
	val priority: Int? = null,

	@field:SerializedName("createdOn")
	val createdOn: String? = null
)
