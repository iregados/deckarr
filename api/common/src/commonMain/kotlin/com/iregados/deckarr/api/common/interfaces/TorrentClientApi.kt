package com.iregados.deckarr.api.common.interfaces

import com.iregados.deckarr.api.common.ApiResult

interface TorrentClientApi : AutoCloseable {
    val config: TorrentClientConfig
    suspend fun getTorrentListAndStats(): ApiResult<TorrentInitialData>
    suspend fun getTorrentListRecentlyActive(): ApiResult<TorrentActivityResponse?>
    suspend fun ping(): ApiResult<Boolean>
    suspend fun stopTorrent(ids: List<String>): ApiResult<Boolean>
    suspend fun resumeTorrent(ids: List<String>): ApiResult<Boolean>
    suspend fun removeTorrent(ids: List<String>, deleteLocalData: Boolean): ApiResult<Boolean>
}