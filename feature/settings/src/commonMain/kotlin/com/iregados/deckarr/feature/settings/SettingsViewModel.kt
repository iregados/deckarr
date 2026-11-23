package com.iregados.deckarr.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iregados.deckarr.api.common.ApiResult
import com.iregados.deckarr.core.domain.DownloadsRepository
import com.iregados.deckarr.core.domain.PreferencesRepository
import com.iregados.deckarr.core.domain.RadarrRepository
import com.iregados.deckarr.core.domain.SonarrRepository
import com.iregados.deckarr.core.util.dto.Notification
import com.iregados.deckarr.core.util.extension.emitError
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val radarrRepository: RadarrRepository,
    private val sonarrRepository: SonarrRepository,
    private val downloadsRepository: DownloadsRepository,
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<SettingsState> = MutableStateFlow(SettingsState())
    val uiState = combine(
        _uiState,
        preferencesRepository.getRadarrSettings(),
        preferencesRepository.getSonarrSettings(),
        preferencesRepository.getDownloadsSettings(),
        preferencesRepository.getTheme()
    ) { currentsState, radarrSettings, sonarrSettings, downloadsSettings, theme ->
        currentsState.copy(
            radarrSettingsState = currentsState.radarrSettingsState.copy(
                connectionAddress = radarrSettings.connectionAddress,
                apiKey = radarrSettings.apiKey,
            ),
            sonarrSettingsState = currentsState.sonarrSettingsState.copy(
                connectionAddress = sonarrSettings.connectionAddress,
                apiKey = sonarrSettings.apiKey
            ),
            downloadsSettingsState = currentsState.downloadsSettingsState.copy(
                selectedClient = downloadsSettings.selectedClient,
                connectionAddress = downloadsSettings.connectionAddress,
                username = downloadsSettings.username,
                password = downloadsSettings.password,
            ),
            currentTheme = theme,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = _uiState.value
    )
    private val _notificationFlow = MutableSharedFlow<Notification>(
        replay = 0,
        extraBufferCapacity = 1
    )
    val notificationFlow = _notificationFlow.asSharedFlow()

    private var job: Job? = null

    fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.TestRadarrConnection -> {
                job?.cancel()
                job = viewModelScope.launch {
                    _uiState.update {
                        it.copy(
                            radarrSettingsState = it.radarrSettingsState.copy(
                                isTesting = true,
                                isEnabled = false
                            )
                        )
                    }
                    val response = radarrRepository.systemStatus(
                        connectionAddress = event.connectionAddress,
                        apiKey = event.apiKey
                    )

                    if (response is ApiResult.Error) {
                        _notificationFlow.emitError("Error while connecting to Radarr: ${response.message}")
                    }

                    _uiState.update {
                        it.copy(
                            radarrSettingsState = it.radarrSettingsState.copy(
                                isTesting = false,
                                isEnabled = response is ApiResult.Success
                            )
                        )
                    }
                }
            }

            is SettingsEvent.SaveRadarrSettings -> {
                viewModelScope.launch {
                    preferencesRepository.setRadarrSettings(
                        connectionAddress = event.connectionAddress,
                        apiKey = event.apiKey
                    )
                    _uiState.update {
                        it.copy(
                            radarrSettingsState = it.radarrSettingsState.copy(
                                isEnabled = false
                            )
                        )
                    }
                    event.onDismiss()
                }
            }

            SettingsEvent.ResetRadarrStatus -> {
                job?.cancel()
                _uiState.update {
                    it.copy(
                        radarrSettingsState = it.radarrSettingsState.copy(
                            isTesting = false,
                            isEnabled = false
                        )
                    )
                }
            }

            is SettingsEvent.TestSonarrConnection -> {
                job?.cancel()
                job = viewModelScope.launch {
                    _uiState.update {
                        it.copy(
                            sonarrSettingsState = it.sonarrSettingsState.copy(
                                isTesting = true,
                                isEnabled = false
                            )
                        )
                    }
                    val response = sonarrRepository.testConnection(
                        connectionAddress = event.connectionAddress,
                        apiKey = event.apiKey
                    )

                    if (response is ApiResult.Error) {
                        _notificationFlow.emitError("Error while connecting to Sonarr: ${response.message}")
                    }

                    _uiState.update {
                        it.copy(
                            sonarrSettingsState = it.sonarrSettingsState.copy(
                                isTesting = false,
                                isEnabled = response is ApiResult.Success
                            )
                        )
                    }

                }
            }

            is SettingsEvent.SaveSonarrSettings -> {
                viewModelScope.launch {
                    preferencesRepository.setSonarrSettings(
                        connectionAddress = event.connectionAddress,
                        apiKey = event.apiKey
                    )
                    _uiState.update {
                        it.copy(
                            sonarrSettingsState = it.sonarrSettingsState.copy(
                                isEnabled = false
                            )
                        )
                    }
                    event.onDismiss()
                }
            }

            SettingsEvent.ResetSonarrStatus -> {
                job?.cancel()
                _uiState.update {
                    it.copy(
                        sonarrSettingsState = it.sonarrSettingsState.copy(
                            isTesting = false,
                            isEnabled = false
                        )
                    )
                }
            }

            is SettingsEvent.TestDownloadsConnection -> {
                job?.cancel()
                job = viewModelScope.launch {
                    _uiState.update {
                        it.copy(
                            downloadsSettingsState = it.downloadsSettingsState.copy(
                                isTesting = true,
                                isEnabled = false
                            )
                        )
                    }
                    val response = downloadsRepository.testConnection(
                        selectedClient = event.selectedClient,
                        connectionAddress = event.connectionAddress,
                        username = event.username,
                        password = event.password
                    )

                    if (response is ApiResult.Error) {
                        _notificationFlow.emitError("Error while connecting to Torrent Client: ${response.message}")
                    }

                    _uiState.update {
                        it.copy(
                            downloadsSettingsState = it.downloadsSettingsState.copy(
                                isTesting = false,
                                isEnabled = response is ApiResult.Success
                            )
                        )
                    }

                }
            }

            is SettingsEvent.SaveDownloadsSettings -> {
                viewModelScope.launch {
                    preferencesRepository.setDownloadsSettings(
                        selectedClient = event.selectedClient,
                        connectionAddress = event.connectionAddress,
                        username = event.username,
                        password = event.password
                    )
                    _uiState.update {
                        it.copy(
                            downloadsSettingsState = it.downloadsSettingsState.copy(
                                isEnabled = false
                            )
                        )
                    }
                    event.onDismiss()
                }
            }

            SettingsEvent.ResetDownloadsStatus -> {
                job?.cancel()
                _uiState.update {
                    it.copy(
                        downloadsSettingsState = it.downloadsSettingsState.copy(
                            isTesting = false,
                            isEnabled = false
                        )
                    )
                }
            }

            is SettingsEvent.SetTheme -> {
                viewModelScope.launch {
                    preferencesRepository.setTheme(event.theme)
                }
            }

        }
    }
}