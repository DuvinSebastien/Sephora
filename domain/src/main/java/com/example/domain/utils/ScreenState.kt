package com.example.domain.utils

import androidx.compose.runtime.Composable
import kotlinx.android.parcel.RawValue

sealed class Resource<out R> {

    object IDLE : Resource<Nothing>()

    data class Success<out T>(val data: @RawValue T?) : Resource<T>()

    data class Error(val exception: Throwable) : Resource<Nothing>()

    object Loading : Resource<Nothing>()


    @Composable
    fun <T> Resource<T>.onLoadingCompose(function: @Composable () -> Unit) {
        if (this is Loading) {
            function.invoke()
        }
    }

    @Composable
    fun <T> Resource<T>.onErrorCompose(function: @Composable (exception: Throwable) -> Unit) {
        if (this is Error) {
            function.invoke(this.exception)
        }
    }

    fun <T> Resource<T>.onValue(function: (result: T) -> Unit) {
        if (this is Success && !this.data.isEmpty()) {
            function.invoke(this.data!!)
        }
    }

    fun <T> Resource<T>.data(): T? {
        return if (this is Success) this.data else null

    }

    @Composable
    fun <T> Resource<T>.onValueCompose(function: @Composable (result: T) -> Unit) {
        if (this is Success && !this.data.isEmpty()) {
            function.invoke(this.data!!)
        }
    }

    @Composable
    fun <T> Resource<T>.onEmptyCompose(
        ignoreEmptyState: Boolean = false,
        function: @Composable () -> Unit,
    ) {
        if (this is Success && this.data.isEmpty() || ignoreEmptyState) {
            function.invoke()
        }
    }

    private fun Any?.isEmpty(): Boolean {
        return this == null ||
                (this is Collection<*> && this.isEmpty())
    }
}