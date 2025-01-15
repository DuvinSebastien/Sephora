package com.example.domain.utils

import kotlinx.android.parcel.RawValue

sealed class Resource<out R> {

    object IDLE : Resource<Nothing>()

    data class Success<out T>(val data: @RawValue T?) : Resource<T>()

    data class Error(val exception: Throwable) : Resource<Nothing>()

    object Loading : Resource<Nothing>()

}