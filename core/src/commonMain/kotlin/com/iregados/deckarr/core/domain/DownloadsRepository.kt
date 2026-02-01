package com.iregados.deckarr.core.domain

import com.iregados.api.common.ApiResult
import com.iregados.api.common.dto.TorrentInitialDataImpl
import com.iregados.api.common.interfaces.TorrentActivityResponse
import com.iregados.api.common.interfaces.TorrentClientApi
import com.iregados.api.common.interfaces.TorrentClientConfig
import com.iregados.api.qbittorrent.QBitTorrentApi
import com.iregados.api.transmission.TransmissionApi
import com.iregados.deckarr.core.domain.util.TorrentClientOption
import io.ktor.http.URLProtocol
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map

class DownloadsRepository(
    val torrentClientApiFlow: StateFlow<TorrentClientApi>,
) {
    fun getConfigFlow(): Flow<TorrentClientConfig> =
        torrentClientApiFlow.map { it.config }

    suspend fun testConnection(
        selectedClient: TorrentClientOption,
        connectionAddress: String,
        username: String,
        password: String
    ): Boolean? {
        if (selectedClient == TorrentClientOption.Transmission) {
            val tempApi = TransmissionApi(
                if (connectionAddress.startsWith("https")) URLProtocol.HTTPS else URLProtocol.HTTP,
                connectionAddress.substringAfter("://"),
                username,
                password
            )
            return when (tempApi.ping()) {
                is ApiResult.Success -> true
                is ApiResult.Error -> null
            }
        } else {
            val tempApi = QBitTorrentApi(
                if (connectionAddress.startsWith("https")) URLProtocol.HTTPS else URLProtocol.HTTP,
                connectionAddress.substringAfter("://"),
                username,
                password
            )
            return when (tempApi.ping()) {
                is ApiResult.Success -> true
                is ApiResult.Error -> null
            }
        }
    }

    suspend fun getTorrentsAndStats(): TorrentInitialDataImpl? {
        return when (
            val res = torrentClientApiFlow.value.getTorrentListAndStats()
        ) {
            is ApiResult.Success -> {
                TorrentInitialDataImpl(
                    res.data.torrentList?.sortedByDescending {
                        it.addedDate
                    },
                    res.data.torrentSessionStats
                )
            }

            is ApiResult.Error -> null
        }
    }

    suspend fun getTorrentsUpdate(): TorrentActivityResponse? {
        return when (
            val res = torrentClientApiFlow.value.getTorrentListRecentlyActive()
        ) {
            is ApiResult.Success -> res.data
            is ApiResult.Error -> null
        }
    }

    suspend fun stopTorrent(ids: List<String>): Boolean? {
        return when (
            torrentClientApiFlow.value.stopTorrent(ids)
        ) {
            is ApiResult.Success -> true
            is ApiResult.Error -> null
        }
    }

    suspend fun resumeTorrent(ids: List<String>): Boolean? {
        return when (
            torrentClientApiFlow.value.resumeTorrent(ids)
        ) {
            is ApiResult.Success -> true
            is ApiResult.Error -> null
        }
    }

    suspend fun removeTorrent(
        ids: List<String>,
        deleteLocalData: Boolean
    ): Boolean? {
        return when (
            torrentClientApiFlow.value.removeTorrent(
                ids,
                deleteLocalData = deleteLocalData
            )
        ) {
            is ApiResult.Success -> true
            is ApiResult.Error -> null
        }
    }
}