package com.iregados.deckarr.core.domain

import com.iregados.api.common.ApiResult
import com.iregados.api.transmission.TransmissionApi
import com.iregados.api.transmission.dto.SessionStats
import com.iregados.api.transmission.dto.Torrent
import com.iregados.api.transmission.dto.TorrentActivityResponse
import com.iregados.api.transmission.dto.TransmissionApiConfig
import com.iregados.api.transmission.methods.getSession
import com.iregados.api.transmission.methods.getStats
import com.iregados.api.transmission.methods.getTorrentList
import com.iregados.api.transmission.methods.getTorrentListRecentlyActive
import com.iregados.api.transmission.methods.removeTorrent
import com.iregados.api.transmission.methods.resumeTorrent
import com.iregados.api.transmission.methods.stopTorrent
import io.ktor.http.URLProtocol
import kotlinx.coroutines.flow.StateFlow

class DownloadsRepository(
    private val transmissionApi: TransmissionApi
) {
    fun getConfigFlow(): StateFlow<TransmissionApiConfig> = transmissionApi.configFlow

    suspend fun testConnection(
        connectionAddress: String,
        username: String,
        password: String
    ): Boolean? {
        val tempApi = TransmissionApi(
            if (connectionAddress.startsWith("https")) URLProtocol.HTTPS else URLProtocol.HTTP,
            connectionAddress.substringAfter("://"),
            username,
            password
        )
        return when (tempApi.getSession()) {
            is ApiResult.Success -> true
            is ApiResult.Error -> null
        }
    }

    suspend fun getTorrents(): List<Torrent>? {
        return when (val res = transmissionApi.getTorrentList()) {
            is ApiResult.Success -> {
                res.data.sortedByDescending {
                    it.addedDate
                }
            }

            is ApiResult.Error -> null
        }
    }

    suspend fun getStats(): SessionStats? {
        return when (val res = transmissionApi.getStats()) {
            is ApiResult.Success -> res.data
            is ApiResult.Error -> null
        }
    }

    suspend fun getTorrentsUpdate(): TorrentActivityResponse? {
        return when (val res = transmissionApi.getTorrentListRecentlyActive()) {
            is ApiResult.Success -> res.data
            is ApiResult.Error -> null
        }
    }

    suspend fun stopTorrent(ids: List<Int>): Boolean? {
        return when (transmissionApi.stopTorrent(ids)) {
            is ApiResult.Success -> true
            is ApiResult.Error -> null
        }
    }

    suspend fun resumeTorrent(ids: List<Int>): Boolean? {
        return when (transmissionApi.resumeTorrent(ids)) {
            is ApiResult.Success -> true
            is ApiResult.Error -> null
        }
    }

    suspend fun removeTorrent(
        ids: List<Int>,
        deleteLocalData: Boolean
    ): Boolean? {
        return when (
            transmissionApi.removeTorrent(ids, deleteLocalData = deleteLocalData)
        ) {
            is ApiResult.Success -> true
            is ApiResult.Error -> null
        }
    }
}