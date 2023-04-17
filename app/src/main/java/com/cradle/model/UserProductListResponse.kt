package com.cradle.model


import android.os.Parcel
import android.os.Parcelable
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class UserProductListResponse(

	@field:SerializedName("productList")
	val productList: List<ProductListItem12?>? = null
) : Parcelable {
	constructor(parcel: Parcel) : this(parcel.createTypedArrayList(ProductListItem12)) {
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeTypedList(productList)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<UserProductListResponse> {
		override fun createFromParcel(parcel: Parcel): UserProductListResponse {
			return UserProductListResponse(parcel)
		}

		override fun newArray(size: Int): Array<UserProductListResponse?> {
			return arrayOfNulls(size)
		}
	}

}

data class MetaData1(

	@field:SerializedName("images")
	val images: List<String?>? = null,

	@field:SerializedName("description")
	val description: String? = null
) : Parcelable {
	constructor(parcel: Parcel) : this(
		parcel.createStringArrayList(),
		parcel.readString()
	) {
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeStringList(images)
		parcel.writeString(description)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<MetaData1> {
		override fun createFromParcel(parcel: Parcel): MetaData1 {
			return MetaData1(parcel)
		}

		override fun newArray(size: Int): Array<MetaData1?> {
			return arrayOfNulls(size)
		}
	}

}

data class ProductListItem12(
	@PrimaryKey(autoGenerate = true)
	@field:SerializedName("productId")
	var productId: Int? = null,

	@field:SerializedName("metaData")
	val metaData: MetaData1? = null,

	@field:SerializedName("discountPercent")
	var discountPercent: Double? = null,

	@field:SerializedName("discountedPrice")
	var discountedPrice: Double? = null,

	@field:SerializedName("actualPrice")
	var price: String? = null,

	@field:SerializedName("discountValue")
	var discountValue: String? = null,


	@field:SerializedName("itemId")
	var itemId: String? = null,

	@field:SerializedName("name")
	var name: String? = null,

	@field:SerializedName("rating")
	val rating: String? = null,

	@field:SerializedName("currency")
	var currency: String? = null,

	@field:SerializedName("category")
	var category: String? = null,

	@field:SerializedName("stock")
	var stock: String? = null,

	@field:SerializedName("qty")
	var qty: String? = null ,

	@field:SerializedName("categoryId")
	var categoryId: String? = null

) : Parcelable {
	constructor(parcel: Parcel) : this(
		parcel.readInt(),
		parcel.readParcelable(MetaData1::class.java.classLoader),
		parcel.readValue(Double::class.java.classLoader) as? Double,
		parcel.readValue(Double::class.java.classLoader) as? Double,
		parcel.readString(),
		parcel.readString(),
		parcel.readString(),
		parcel.readString(),
		parcel.readString(),
		parcel.readString(),
		parcel.readString(),
		parcel.readString(),
		parcel.readString(),
		parcel.readString()
	) {
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeParcelable(metaData, flags)
		parcel.writeValue(discountPercent)
		parcel.writeInt(productId!!)
		parcel.writeValue(discountedPrice)
		parcel.writeString(price)
		parcel.writeString(discountValue)
		parcel.writeString(itemId)
		parcel.writeString(name)
		parcel.writeString(rating)
		parcel.writeString(currency)
		parcel.writeString(category)
		parcel.writeString(stock)
		parcel.writeString(qty)
		parcel.writeString(categoryId)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<ProductListItem12> {
		override fun createFromParcel(parcel: Parcel): ProductListItem12 {
			return ProductListItem12(parcel)
		}

		override fun newArray(size: Int): Array<ProductListItem12?> {
			return arrayOfNulls(size)
		}
	}

}