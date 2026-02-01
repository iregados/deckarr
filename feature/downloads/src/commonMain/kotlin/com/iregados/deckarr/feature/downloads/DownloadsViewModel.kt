package com.iregados.deckarr.feature.downloads

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iregados.api.common.dto.TorrentImpl
import com.iregados.deckarr.core.domain.DownloadsRepository
import com.iregados.deckarr.core.util.dto.Notification
import com.iregados.deckarr.core.util.extension.emitError
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds

class DownloadsViewModel(
    private val downloadsRepository: DownloadsRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<DownloadsState> = MutableStateFlow(DownloadsState())
    val uiState = _uiState
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _uiState.value
        )

    private val _notificationFlow = MutableSharedFlow<Notification>(replay = 0)
    val notificationFlow = _notificationFlow.asSharedFlow()
    private var stopObserver = false
    private var observerJob: Job? = null

    private var lastCallTime = Clock.System.now()

    init {
        downloadsRepository.getConfigFlow()
            .onEach { apiConfig ->
                if (apiConfig.host.isEmpty() ||
                    apiConfig.username.isEmpty() ||
                    apiConfig.password.isEmpty()
                ) {
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            needsConfiguration = true
                        )
                    }
                } else {
                    _uiState.update { it.copy(needsConfiguration = false) }
                    loadData()
                }
            }.stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                null
            )
    }

    fun loadData() {
        if (_uiState.value.needsConfiguration) return
        _uiState.update { it.copy(isLoading = true) }
        getAllData()
    }

    fun startObserving() {
        observerJob?.cancel()
        observerJob = viewModelScope.launch {
            while (true) {
                if (_uiState.value.needsConfiguration || stopObserver) {
                    delay(500)
                    continue
                }

                val now = Clock.System.now()
                if (now >= lastCallTime.plus(60.seconds)) {
                    getAllData()
                } else {
                    getUpdate()
                }
                lastCallTime = Clock.System.now()

                delay(2_000)
            }
        }
    }

    fun stopObserving() {
        observerJob?.cancel()
    }

    fun getAllData() {
        viewModelScope.launch {
            stopObserver = true
            val initialData = downloadsRepository.getTorrentsAndStats()
            val torrents = initialData?.torrentList
            val stats = initialData?.torrentSessionStats

            val totalDownloadSpeed = torrents?.sumOf { it.rateDownload ?: 0 }
            val totalUploadSpeed = torrents?.sumOf { it.rateUpload ?: 0 }

            _uiState.update { state ->
                state.copy(
                    isLoading = false,
                    torrents = torrents,
                    activeTorrents = torrents?.filter {
                        it.rateDownload != 0 || it.rateUpload != 0
                    },
                    downloadingTorrents = torrents?.filter {
                        (it.percentDone ?: 0f) < 1f
                    },
                    totalDownloadSpeed = totalDownloadSpeed,
                    totalUploadSpeed = totalUploadSpeed,
                    stats = stats
                )
            }
            stopObserver = false
        }
    }

    suspend fun getUpdate() {
        val update = downloadsRepository.getTorrentsUpdate()

        update?.let { response ->
            _uiState.value.torrents?.let { oldList ->
                val activeTorrents = response.torrents
                val removed = response.removed

                val updateMap = activeTorrents.associateBy { it.id }
                val newList = (oldList.map { torrent ->
                    val updatedTorrent = updateMap[torrent.id]
                    if (updatedTorrent != null) {
                        (torrent as TorrentImpl).copy(
                            name = updatedTorrent.name ?: torrent.name,
                            addedDate = updatedTorrent.addedDate ?: torrent.addedDate,
                            status = updatedTorrent.status ?: torrent.status,
                            totalSize = updatedTorrent.totalSize ?: torrent.totalSize,
                            percentDone = updatedTorrent.percentDone ?: torrent.percentDone,
                            rateDownload = updatedTorrent.rateDownload ?: torrent.rateDownload,
                            rateUpload = updatedTorrent.rateUpload ?: torrent.rateUpload,
                            uploadedEver = updatedTorrent.uploadedEver ?: torrent.uploadedEver,
                            downloadedEver = updatedTorrent.downloadedEver
                                ?: torrent.downloadedEver,
                            eta = updatedTorrent.eta ?: torrent.eta,
                            isFinished = updatedTorrent.isFinished ?: torrent.isFinished,
                            isStalled = updatedTorrent.isStalled ?: torrent.isStalled
                        )
                    } else {
                        torrent
                    }
                } + activeTorrents.filter { updateTorrent ->
                    oldList.none { it.id == updateTorrent.id }
                })
                    .filter { torrent -> torrent.id !in removed }
                    .sortedByDescending { it.addedDate }

                val totalDownloadSpeed = newList.sumOf { it.rateDownload ?: 0 }
                val totalUploadSpeed = newList.sumOf { it.rateUpload ?: 0 }

                _uiState.update { state ->
                    state.copy(
                        torrents = newList,
                        activeTorrents = newList.filter {
                            it.rateDownload != 0 || it.rateUpload != 0
                        },
                        downloadingTorrents = newList.filter {
                            (it.percentDone ?: 0f) < 1f
                        },
                        totalDownloadSpeed = totalDownloadSpeed,
                        totalUploadSpeed = totalUploadSpeed
                    )
                }
            }
        }
    }

    fun onEvent(event: DownloadsEvent) {
        when (event) {
            is DownloadsEvent.SetStopTorrent -> {
                viewModelScope.launch {
                    stopObserver = true
                    _uiState.update {
                        it.copy(
                            isUpdatingTorrentId = event.torrentId
                        )
                    }
                    val res = downloadsRepository.stopTorrent(listOf(event.torrentId))
                    if (res == null) {
                        stopObserver = false
                        _notificationFlow.emitError("Error while stopping torrent")
                    } else {
                        delay(500)
                        stopObserver = false
                        getUpdate()
                    }
                    _uiState.update {
                        it.copy(
                            isUpdatingTorrentId = null
                        )
                    }
                }
            }

            is DownloadsEvent.SetResumeTorrent -> {
                viewModelScope.launch {
                    stopObserver = true
                    _uiState.update {
                        it.copy(
                            isUpdatingTorrentId = event.torrentId
                        )
                    }
                    val res = downloadsRepository.resumeTorrent(listOf(event.torrentId))
                    if (res == null) {
                        stopObserver = false
                        _notificationFlow.emitError("Error while resuming torrent")
                    } else {
                        delay(500)
                        stopObserver = false
                        getUpdate()
                    }
                    _uiState.update {
                        it.copy(
                            isUpdatingTorrentId = null
                        )
                    }
                }
            }

            is DownloadsEvent.SetRemoveTorrent -> {
                viewModelScope.launch {
                    stopObserver = true
                    val res = downloadsRepository.removeTorrent(
                        listOf(event.torrentId),
                        event.deleteFiles
                    )
                    if (res == null) {
                        stopObserver = false
                        _notificationFlow.emitError("Error while removing torrent")
                    } else {
                        delay(500)
                        stopObserver = false
                        getUpdate()
                    }
                }
            }

            is DownloadsEvent.SetSelectedTab -> {
                _uiState.update { it.copy(selectedTab = event.tab) }
            }
        }
    }
}
