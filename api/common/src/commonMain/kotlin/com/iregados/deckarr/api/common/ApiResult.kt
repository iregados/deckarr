package com.iregados.deckarr.api.common

sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(val error: ApiError) : ApiResult<Nothing>() {
        constructor(message: String) : this(ApiError.Unknown(message))

        val message: String get() = error.message
    }

    fun getOrNull(): T? = when (this) {
        is Success -> data
        is Error -> null
    }
}
