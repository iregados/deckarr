package com.iregados.deckarr.api.common.util

import com.iregados.deckarr.api.common.ApiResult

inline fun <T, R> ApiResult<T>.map(transform: (T) -> R): ApiResult<R> = when (this) {
    is ApiResult.Success -> ApiResult.Success(transform(data))
    is ApiResult.Error -> this
}