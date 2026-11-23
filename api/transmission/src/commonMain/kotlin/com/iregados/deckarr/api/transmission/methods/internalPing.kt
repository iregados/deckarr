package com.iregados.deckarr.api.transmission.methods

import com.iregados.deckarr.api.common.ApiResult
import com.iregados.deckarr.api.transmission.TransmissionApi
import com.iregados.deckarr.api.transmission.dto.internal.SessionGetRequest
import com.iregados.deckarr.api.transmission.dto.internal.SessionGetResponse

suspend fun TransmissionApi.internalPing(): ApiResult<Boolean> {
    val request = SessionGetRequest()

    return when (
        val response = rpcCall<SessionGetRequest, SessionGetResponse>(request)
    ) {
        is ApiResult.Success -> ApiResult.Success(true)
        is ApiResult.Error -> ApiResult.Error(response.message)
    }
}