package com.cradle.model

import com.google.gson.annotations.SerializedName

data class CountryListResponse(

	@field:SerializedName("countryColorCodeList")
	val countryColorCodeList: List<CountryColorCodeListItem?>? = null,

	@field:SerializedName("success")
	val success: Boolean? = null
)

data class CountryColorCodeListItem(

	@field:SerializedName("flag")
	val flag: String? = null,

	@field:SerializedName("colorCode")
	val colorCode: String? = null,

	@field:SerializedName("countryName")
	val countryName: String? = null,

	@field:SerializedName("active")
	val active: Boolean? = null
)
