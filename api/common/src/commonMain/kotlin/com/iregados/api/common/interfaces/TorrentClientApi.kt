package com.iregados.api.common.interfaces

import com.iregados.api.common.ApiResult

interface TorrentClientApi {
    val config: TorrentClientConfig
    suspend fun getTorrentListAndStats(): ApiResult<TorrentInitialData>
    suspend fun getTorrentListRecentlyActive(): ApiResult<TorrentActivityResponse?>
    suspend fun ping(): ApiResult<Boolean>
    suspend fun stopTorrent(ids: List<String>): ApiResult<Boolean>
    suspend fun resumeTorrent(ids: List<String>): ApiResult<Boolean>
    suspend fun removeTorrent(ids: List<String>, deleteLocalData: Boolean): ApiResult<Boolean>
}