package com.iregados.deckarr.core.domain

import com.iregados.deckarr.api.common.ApiResult
import com.iregados.deckarr.api.common.dto.TorrentInitialDataImpl
import com.iregados.deckarr.api.common.interfaces.TorrentActivityResponse
import com.iregados.deckarr.api.common.interfaces.TorrentClientConfig
import com.iregados.deckarr.core.domain.dto.DownloadsDataState
import com.iregados.deckarr.core.domain.util.TorrentClientOption
import com.iregados.deckarr.core.util.dto.Notification
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface DownloadsRepository {
    val dataState: StateFlow<DownloadsDataState>
    val notificationFlow: SharedFlow<Notification>

    fun getConfigFlow(): Flow<TorrentClientConfig>

    suspend fun testConnection(
        selectedClient: TorrentClientOption,
        connectionAddress: String,
        username: String,
        password: String
    ): ApiResult<Boolean>

    suspend fun refresh(showLoading: Boolean = true): ApiResult<Boolean>

    suspend fun refreshUpdate(): ApiResult<Boolean>

    suspend fun getTorrentsAndStats(): ApiResult<TorrentInitialDataImpl>

    suspend fun getTorrentsUpdate(): ApiResult<TorrentActivityResponse?>

    suspend fun stopTorrent(ids: List<String>): ApiResult<Boolean>

    suspend fun resumeTorrent(ids: List<String>): ApiResult<Boolean>

    suspend fun removeTorrent(
        ids: List<String>,
        deleteLocalData: Boolean
    ): ApiResult<Boolean>
}