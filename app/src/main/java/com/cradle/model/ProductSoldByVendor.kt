package com.cradle.model

import android.os.Parcel
import android.os.Parcelable

data class ProductSoldByVendor(
	val success: Boolean? = null,
	val count: Int? = null,
	val productList: List<ProductListItem1?>? = null
)

data class ProductSoldListItem(
	val productId: String? = null,
	val actualPrice: Double? = null,
	val rating: Double? = null,
	val vendorId: String? = null,
	val wishlisted: Boolean? = null,
	val vendorName: String? = null,
	val itemId: String? = null,
	val metaData: ProductSoldMetaData? = null,
	val discountedPrice: Double? = null,
	val success: Boolean? = null,
	val name: String? = null,
	val currency: String? = null,
	val discountType: String? = null,
	val stock: Int? = null,
	val dimension: String? = null,
	val discountValue: Double? = null,
	val categoryId: String? = null
):Parcelable {
	constructor(parcel: Parcel) : this(
		parcel.readString(),
		parcel.readValue(Double::class.java.classLoader) as? Double,
		parcel.readValue(Double::class.java.classLoader) as? Double,
		parcel.readString(),
		parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
		parcel.readString(),
		parcel.readString(),
		parcel.readParcelable(ProductSoldMetaData::class.java.classLoader),
		parcel.readValue(Double::class.java.classLoader) as? Double,
		parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
		parcel.readString(),
		parcel.readString(),
		parcel.readString(),
		parcel.readValue(Int::class.java.classLoader) as? Int,
		parcel.readString(),
		parcel.readValue(Double::class.java.classLoader) as? Double,
		parcel.readString()
	) {
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeString(productId)
		parcel.writeValue(actualPrice)
		parcel.writeValue(rating)
		parcel.writeString(vendorId)
		parcel.writeValue(wishlisted)
		parcel.writeString(vendorName)
		parcel.writeString(itemId)
		parcel.writeParcelable(metaData, flags)
		parcel.writeValue(discountedPrice)
		parcel.writeValue(success)
		parcel.writeString(name)
		parcel.writeString(currency)
		parcel.writeString(discountType)
		parcel.writeValue(stock)
		parcel.writeString(dimension)
		parcel.writeValue(discountValue)
		parcel.writeString(categoryId)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<ProductSoldListItem> {
		override fun createFromParcel(parcel: Parcel): ProductSoldListItem {
			return ProductSoldListItem(parcel)
		}

		override fun newArray(size: Int): Array<ProductSoldListItem?> {
			return arrayOfNulls(size)
		}
	}
}

data class ProductSoldMetaData(
	val images: List<String?>? = null,
	val description: String? = null
):Parcelable {
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

	companion object CREATOR : Parcelable.Creator<ProductSoldMetaData> {
		override fun createFromParcel(parcel: Parcel): ProductSoldMetaData {
			return ProductSoldMetaData(parcel)
		}

		override fun newArray(size: Int): Array<ProductSoldMetaData?> {
			return arrayOfNulls(size)
		}
	}
}

