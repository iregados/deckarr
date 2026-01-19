package com.iregados.api.transmission.methods

import com.iregados.api.common.ApiResult
import com.iregados.api.transmission.TransmissionApi
import com.iregados.api.transmission.dto.internal.SessionGetRequest
import com.iregados.api.transmission.dto.internal.SessionGetResponse

suspend fun TransmissionApi.getSession(): ApiResult<Boolean> {
    val request = SessionGetRequest()

    return when (
        val response = rpcCall<SessionGetRequest, SessionGetResponse>(request)
    ) {
        is ApiResult.Success -> ApiResult.Success(true)
        is ApiResult.Error -> ApiResult.Error(response.message)
    }
}