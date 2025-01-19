package com.example.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Brand(@SerializedName("id") val id: String, @SerializedName("name") val name: String) :
    Parcelable