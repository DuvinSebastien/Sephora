package com.example.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductReview(
    @SerializedName("product_id") val productId: Int,
    @SerializedName("hide") val hide: Boolean? = null,
    @SerializedName("reviews") val reviews: List<Review>
) : Parcelable

@Parcelize
data class Review(
    @SerializedName("name") val name: String? = null,
    @SerializedName("text") val text: String? = null,
    @SerializedName("rating") val rating: Float? = null
) : Parcelable