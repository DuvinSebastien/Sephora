package com.example.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImagesUrl(
    @SerializedName("small") val small: String,
    @SerializedName("large") val large: String
) : Parcelable