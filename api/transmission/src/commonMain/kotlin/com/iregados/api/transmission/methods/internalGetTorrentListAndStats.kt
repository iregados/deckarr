package com.iregados.api.transmission.methods

import com.iregados.api.common.ApiResult
import com.iregados.api.common.dto.TorrentInitialDataImpl
import com.iregados.api.common.interfaces.TorrentInitialData
import com.iregados.api.transmission.TransmissionApi
import com.iregados.api.transmission.dto.TransmissionActivityResponse
import com.iregados.api.transmission.dto.internal.SessionStatsRequest
import com.iregados.api.transmission.dto.internal.SessionStatsResponse
import com.iregados.api.transmission.dto.internal.TorrentListRequest
import com.iregados.api.transmission.dto.toTorrentSessionStats

suspend fun TransmissionApi.internalGetTorrentListAndStats(): ApiResult<TorrentInitialData> {
    val torrentListResponse =
        rpcCall<TorrentListRequest, TransmissionActivityResponse>(TorrentListRequest())
    val statsResponse =
        rpcCall<SessionStatsRequest, SessionStatsResponse>(SessionStatsRequest())

    return when (torrentListResponse) {
        is ApiResult.Success if statsResponse is ApiResult.Success -> {
            ApiResult.Success(
                TorrentInitialDataImpl(
                    torrentList = torrentListResponse.data.torrents,
                    torrentSessionStats = statsResponse.data.arguments.toTorrentSessionStats()
                )
            )
        }

        is ApiResult.Error if statsResponse is ApiResult.Error -> {
            ApiResult.Error(
                "torrentList error: ${torrentListResponse.message} + stats error: ${statsResponse.message}"
            )
        }

        else -> {
            ApiResult.Error("Unknown error")
        }
    }

}