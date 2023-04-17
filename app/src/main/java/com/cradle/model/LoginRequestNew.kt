package com.cradle.model

import com.google.gson.annotations.SerializedName

data class LoginRequestNew(

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("emailMobile")
	val emailMobile: String? = null,

	@field:SerializedName("channel")
	val channel: String? = null,

	@field:SerializedName("fcmKey")
	val fcmKey: String? = null
)
