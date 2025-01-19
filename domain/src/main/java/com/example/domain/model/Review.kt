package com.example.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Review(
    @SerializedName("name") val name: String? = null,
    @SerializedName("text") val text: String? = null,
    @SerializedName("rating") val rating: Float? = null
) : Parcelable