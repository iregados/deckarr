package com.iregados.deckarr.api.transmission.methods

import com.iregados.deckarr.api.common.ApiResult
import com.iregados.deckarr.api.common.dto.TorrentInitialDataImpl
import com.iregados.deckarr.api.common.interfaces.TorrentInitialData
import com.iregados.deckarr.api.transmission.TransmissionApi
import com.iregados.deckarr.api.transmission.dto.TransmissionActivityResponse
import com.iregados.deckarr.api.transmission.dto.internal.SessionStatsRequest
import com.iregados.deckarr.api.transmission.dto.internal.SessionStatsResponse
import com.iregados.deckarr.api.transmission.dto.internal.TorrentListRequest
import com.iregados.deckarr.api.transmission.dto.toTorrentSessionStats

suspend fun TransmissionApi.internalGetTorrentListAndStats(): ApiResult<TorrentInitialData> {
    val torrentListResponse =
        rpcCall<TorrentListRequest, TransmissionActivityResponse>(TorrentListRequest())
    val statsResponse =
        rpcCall<SessionStatsRequest, SessionStatsResponse>(SessionStatsRequest())

    val torrentList = when (torrentListResponse) {
        is ApiResult.Success -> torrentListResponse.data.torrents
        is ApiResult.Error -> return ApiResult.Error(torrentListResponse.message)
    }

    val stats = when (statsResponse) {
        is ApiResult.Success -> statsResponse.data.arguments.toTorrentSessionStats()
        is ApiResult.Error -> return ApiResult.Error(statsResponse.message)
    }

    return ApiResult.Success(
        TorrentInitialDataImpl(
            torrentList = torrentList,
            torrentSessionStats = stats
        )
    )
}