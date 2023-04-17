package com.cradle.model.allcountry

import com.google.gson.annotations.SerializedName

data class AllCountryListResponse(

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("countryList")
	val countryList: List<CountryListItem?>? = null
)

data class CountryListItem(

	@field:SerializedName("isdCode")
	val isdCode: String? = null,

	@field:SerializedName("countryName")
	val countryName: String? = null
)
