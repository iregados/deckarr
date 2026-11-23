package com.iregados.deckarr.api.common.util

import com.iregados.deckarr.api.common.ApiError
import io.ktor.client.plugins.ResponseException
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.SerializationException
import kotlin.coroutines.cancellation.CancellationException

fun Throwable.toApiError(): ApiError = when (this) {
    is CancellationException -> throw this
    is ResponseException -> when (response.status) {
        HttpStatusCode.Unauthorized, HttpStatusCode.Forbidden ->
            ApiError.Unauthorized(response.status.description)

        HttpStatusCode.NotFound ->
            ApiError.NotFound(response.status.description)

        else ->
            ApiError.Server(response.status.value, response.status.description)
    }

    is SerializationException ->
        ApiError.Serialization(message ?: "Invalid response")

    else ->
        ApiError.Network(message ?: "Unknown error")
}