package com.example.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    @SerializedName("product_id") val id: Int,
    @SerializedName("product_name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("price") val price: Double,
    @SerializedName("images_url") val imagesUrl: ImagesUrl,
    @SerializedName("c_brand") val brand: Brand,
    @SerializedName("is_productSet") val isProductSet: Boolean,
    @SerializedName("is_special_brand") val isSpecialBrand: Boolean,
) : Parcelable

@Parcelize
data class Brand(@SerializedName("id") val id: String, @SerializedName("name") val name: String) :
    Parcelable

@Parcelize
data class ImagesUrl(
    @SerializedName("small") val small: String,
    @SerializedName("large") val large: String
) : Parcelable