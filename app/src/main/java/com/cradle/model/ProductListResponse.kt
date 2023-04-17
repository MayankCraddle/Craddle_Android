package com.cradle.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cradle.model.trade.VariantsItem
import com.google.gson.annotations.SerializedName

data class ProductListResponse(

    @field:SerializedName("productList")
    val productList: List<ProductListItem1?>? = null
)

data class MetaDataNew(
    @field:SerializedName("description")
    val description: String? = null,
    @field:SerializedName("images")
    val images: List<String?>? = null

)

@Entity(tableName = "productlist")
data class ProductListItem1(
    @PrimaryKey(autoGenerate = true)
    @field:SerializedName("stock")
    val stock: Int? = null,

    @field:SerializedName("productId")
    val productId: String? = null,

    @field:SerializedName("actualPrice")
    val actualPrice: Double? = null,

    @field:SerializedName("rating")
    val rating: Double? = null,

    @field:SerializedName("itemId")
    val itemId: String? = null,


    @field:SerializedName("metaData")
    val metaData: MetaDataNew? = null,

    @field:SerializedName("discountedPrice")
    val discountedPrice: Double? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("currency")
    val currency: String? = null,

    @field:SerializedName("discountType")
    val discountType: String? = null,

    @field:SerializedName("category")
    val category: String? = null,


    @field:SerializedName("dimension")
    val dimension: String? = null,

    @field:SerializedName("orderable")
    val orderable: String? = null,
    @field:SerializedName("variants")
    val variants: List<VariantsItem?>? = null,

    @field:SerializedName("discountValue")
    val discountValue: Double? = null
)
