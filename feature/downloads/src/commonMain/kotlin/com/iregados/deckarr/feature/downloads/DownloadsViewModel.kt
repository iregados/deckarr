package com.iregados.deckarr.feature.downloads

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iregados.deckarr.api.common.ApiResult
import com.iregados.deckarr.core.domain.DownloadsRepository
import com.iregados.deckarr.core.util.dto.Notification
import com.iregados.deckarr.core.util.extension.emitError
import com.iregados.deckarr.feature.downloads.utils.withTorrents
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class DownloadsViewModel(
    private val downloadsRepository: DownloadsRepository
) : ViewModel() {
    private val _isUpdatingTorrentId = MutableStateFlow<String?>(null)
    private val _selectedTab =
        MutableStateFlow<DownloadsTabsItems>(DownloadsTabsItems.All(items = null))

    val uiState: StateFlow<DownloadsState> = combine(
        downloadsRepository.dataState,
        _isUpdatingTorrentId,
        _selectedTab,
    ) { data, updatingTorrentId, selectedTab ->
        DownloadsState(
            isLoading = data.isLoading,
            isUpdatingTorrentId = updatingTorrentId,
            needsConfiguration = data.needsConfiguration,
            selectedTab = selectedTab,
            stats = data.stats,
        ).withTorrents(data.torrents)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        DownloadsState(
            isLoading = downloadsRepository.dataState.value.isLoading,
            needsConfiguration = downloadsRepository.dataState.value.needsConfiguration,
            stats = downloadsRepository.dataState.value.stats,
        ).withTorrents(downloadsRepository.dataState.value.torrents)
    )

    private val _notificationFlow = MutableSharedFlow<Notification>(replay = 0)
    val notificationFlow = _notificationFlow.asSharedFlow()

    private var stopObserver = false
    private var observerJob: Job? = null

    private var lastFullRefreshTime = Clock.System.now()

    fun startObserving() {
        observerJob?.cancel()
        observerJob = viewModelScope.launch {
            while (true) {
                if (downloadsRepository.dataState.value.needsConfiguration || stopObserver) {
                    delay(500.milliseconds)
                    continue
                }

                val now = Clock.System.now()
                if (now >= lastFullRefreshTime.plus(60.seconds)) {
                    downloadsRepository.refresh(showLoading = false)
                    lastFullRefreshTime = Clock.System.now()
                } else {
                    downloadsRepository.refreshUpdate()
                }

                delay(2.seconds)
            }
        }
    }

    fun stopObserving() {
        observerJob?.cancel()
    }

    fun onEvent(event: DownloadsEvent) {
        when (event) {
            is DownloadsEvent.SetStopTorrent -> {
                viewModelScope.launch {
                    stopObserver = true
                    _isUpdatingTorrentId.value = event.torrentId
                    try {
                        val result = downloadsRepository.stopTorrent(listOf(event.torrentId))
                        if (result is ApiResult.Error) {
                            _notificationFlow.emitError("Error while stopping torrent: ${result.message}")
                        } else {
                            delay(500.milliseconds)
                            downloadsRepository.refreshUpdate()
                        }
                    } finally {
                        stopObserver = false
                        _isUpdatingTorrentId.value = null
                    }
                }
            }

            is DownloadsEvent.SetResumeTorrent -> {
                viewModelScope.launch {
                    stopObserver = true
                    _isUpdatingTorrentId.value = event.torrentId
                    try {
                        val result = downloadsRepository.resumeTorrent(listOf(event.torrentId))
                        if (result is ApiResult.Error) {
                            _notificationFlow.emitError("Error while resuming torrent: ${result.message}")
                        } else {
                            delay(500.milliseconds)
                            downloadsRepository.refreshUpdate()
                        }
                    } finally {
                        stopObserver = false
                        _isUpdatingTorrentId.value = null
                    }
                }
            }

            is DownloadsEvent.SetRemoveTorrent -> {
                viewModelScope.launch {
                    stopObserver = true
                    try {
                        val result = downloadsRepository.removeTorrent(
                            listOf(event.torrentId),
                            event.deleteFiles
                        )
                        if (result is ApiResult.Error) {
                            _notificationFlow.emitError("Error while removing torrent: ${result.message}")
                        } else {
                            delay(500.milliseconds)
                            downloadsRepository.refreshUpdate()
                        }
                    } finally {
                        stopObserver = false
                    }
                }
            }

            is DownloadsEvent.SetSelectedTab -> {
                _selectedTab.value = event.tab
            }
        }
    }
}