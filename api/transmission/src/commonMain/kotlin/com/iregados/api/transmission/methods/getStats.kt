package com.iregados.api.transmission.methods

import com.iregados.api.common.ApiResult
import com.iregados.api.transmission.TransmissionApi
import com.iregados.api.transmission.dto.SessionStats
import com.iregados.api.transmission.dto.internal.SessionStatsRequest
import com.iregados.api.transmission.dto.internal.SessionStatsResponse

suspend fun TransmissionApi.getStats(): ApiResult<SessionStats> {
    val request = SessionStatsRequest()

    return when (
        val response = rpcCall<SessionStatsRequest, SessionStatsResponse>(request)
    ) {
        is ApiResult.Success -> ApiResult.Success(response.data.arguments)
        is ApiResult.Error -> ApiResult.Error(response.message)
    }
}