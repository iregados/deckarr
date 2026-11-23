package com.iregados.deckarr.core.data.repository

import com.iregados.deckarr.api.common.ApiResult
import com.iregados.deckarr.api.common.dto.TorrentInitialDataImpl
import com.iregados.deckarr.api.common.interfaces.TorrentClientApi
import com.iregados.deckarr.api.common.interfaces.TorrentClientConfig
import com.iregados.deckarr.api.common.util.map
import com.iregados.deckarr.core.domain.DownloadsRepository
import com.iregados.deckarr.core.domain.TorrentClientFactory
import com.iregados.deckarr.core.domain.dto.DownloadsDataState
import com.iregados.deckarr.core.domain.dto.DownloadsSettings
import com.iregados.deckarr.core.domain.util.TorrentClientOption
import com.iregados.deckarr.core.util.dto.Notification
import com.iregados.deckarr.core.util.extension.emitError
import com.iregados.deckarr.core.util.extension.mergeTorrents
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class DownloadsRepositoryImpl(
    val torrentClientApiFlow: StateFlow<TorrentClientApi?>,
    private val torrentClientFactory: TorrentClientFactory,
    private val scope: CoroutineScope,
) : DownloadsRepository {
    private val _dataState = MutableStateFlow(DownloadsDataState())
    override val dataState = _dataState.asStateFlow()

    private val _notificationFlow = MutableSharedFlow<Notification>(
        replay = 0,
        extraBufferCapacity = 4
    )
    override val notificationFlow = _notificationFlow.asSharedFlow()

    private val syncMutex = Mutex()

    init {
        scope.launch {
            torrentClientApiFlow.filterNotNull().collectLatest { api ->
                if (api.config.host.isEmpty()) {
                    _dataState.update {
                        DownloadsDataState(
                            isLoading = false,
                            isConfigLoaded = true,
                            needsConfiguration = true,
                        )
                    }
                } else {
                    _dataState.update {
                        DownloadsDataState(
                            isLoading = true,
                            isConfigLoaded = true,
                            needsConfiguration = false,
                        )
                    }
                    refresh()
                }
            }
        }
    }

    override fun getConfigFlow(): Flow<TorrentClientConfig> =
        torrentClientApiFlow.filterNotNull().map { it.config }

    private suspend fun client(): TorrentClientApi =
        torrentClientApiFlow.filterNotNull().first()

    override suspend fun refresh(showLoading: Boolean): ApiResult<Boolean> {
        if (_dataState.value.needsConfiguration) {
            return ApiResult.Success(false)
        }

        if (showLoading || _dataState.value.torrents == null) {
            _dataState.update { it.copy(isLoading = true, error = null) }
        }

        return syncMutex.withLock {
            val result = getTorrentsAndStats()
            val data = result.getOrNull()

            _dataState.update { state ->
                state.copy(
                    isLoading = false,
                    torrents = data?.torrentList ?: state.torrents,
                    stats = data?.torrentSessionStats ?: state.stats,
                    error = (result as? ApiResult.Error)?.message
                )
            }

            val error = result as? ApiResult.Error
            if (error != null && _dataState.value.torrents != null) {
                _notificationFlow.emitError("Error while loading torrents: ${error.message}")
            }

            when (result) {
                is ApiResult.Success -> ApiResult.Success(true)
                is ApiResult.Error -> result
            }
        }
    }

    override suspend fun refreshUpdate(): ApiResult<Boolean> {
        if (_dataState.value.needsConfiguration) {
            return ApiResult.Success(false)
        }

        return syncMutex.withLock {
            val oldList = _dataState.value.torrents ?: return@withLock ApiResult.Success(false)
            val result = getTorrentsUpdate()
            val update = result.getOrNull() ?: return@withLock when (result) {
                is ApiResult.Success -> ApiResult.Success(false)
                is ApiResult.Error -> result
            }

            val newList = oldList.mergeTorrents(update)
            _dataState.update { state ->
                state.copy(torrents = newList)
            }
            ApiResult.Success(true)
        }
    }

    override suspend fun testConnection(
        selectedClient: TorrentClientOption,
        connectionAddress: String,
        username: String,
        password: String
    ): ApiResult<Boolean> {
        val settings = DownloadsSettings(
            selectedClient = selectedClient,
            connectionAddress = connectionAddress,
            username = username,
            password = password
        )

        return torrentClientFactory.create(settings).use { it.ping() }
    }

    override suspend fun getTorrentsAndStats() = client().getTorrentListAndStats()
        .map { data ->
            TorrentInitialDataImpl(
                data.torrentList?.sortedByDescending { it.addedDate },
                data.torrentSessionStats
            )
        }

    override suspend fun getTorrentsUpdate() =
        client().getTorrentListRecentlyActive()

    override suspend fun stopTorrent(ids: List<String>) =
        client().stopTorrent(ids)

    override suspend fun resumeTorrent(ids: List<String>) =
        client().resumeTorrent(ids)

    override suspend fun removeTorrent(
        ids: List<String>,
        deleteLocalData: Boolean
    ): ApiResult<Boolean> {
        val result = client().removeTorrent(ids, deleteLocalData)
        if (result is ApiResult.Success && result.data) {
            _dataState.update { state ->
                state.copy(
                    torrents = state.torrents?.filterNot { it.id in ids }
                )
            }
        }
        return result
    }
}