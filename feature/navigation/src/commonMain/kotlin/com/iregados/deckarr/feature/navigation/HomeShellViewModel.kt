package com.iregados.deckarr.feature.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iregados.deckarr.core.domain.DownloadsRepository
import com.iregados.deckarr.core.domain.RadarrRepository
import com.iregados.deckarr.core.domain.SonarrRepository
import com.iregados.deckarr.core.util.dto.Notification
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch

class HomeShellViewModel(
    private val radarrRepository: RadarrRepository,
    private val sonarrRepository: SonarrRepository,
    private val downloadsRepository: DownloadsRepository,
) : ViewModel() {
    val notificationFlow: SharedFlow<Notification> = merge(
        radarrRepository.notificationFlow,
        sonarrRepository.notificationFlow,
        downloadsRepository.notificationFlow,
    ).shareIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        replay = 0,
    )

    fun refresh(currentTab: BottomNavTab) {
        viewModelScope.launch {
            when (currentTab) {
                BottomNavTab.MOVIES -> radarrRepository.refresh()
                BottomNavTab.SERIES -> sonarrRepository.refresh()
                BottomNavTab.DOWNLOADS -> downloadsRepository.refresh()
                BottomNavTab.HOME -> {
                    launch { radarrRepository.refresh() }
                    launch { sonarrRepository.refresh() }
                    launch { downloadsRepository.refresh() }
                }
            }
        }
    }
}