package com.iregados.deckarr.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iregados.deckarr.core.domain.DownloadsRepository
import com.iregados.deckarr.core.domain.PreferencesRepository
import com.iregados.deckarr.core.domain.RadarrRepository
import com.iregados.deckarr.core.domain.SonarrRepository
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
        preferencesRepository.getTransmissionSettings(),
        preferencesRepository.getTheme()
    ) { currentsState, radarrSettings, sonarrSettings, transmissionSettings, theme ->
        currentsState.copy(
            radarrSettingsState = currentsState.radarrSettingsState.copy(
                connectionAddress = radarrSettings.connectionAddress,
                apiKey = radarrSettings.apiKey,
            ),
            sonarrSettingsState = currentsState.sonarrSettingsState.copy(
                connectionAddress = sonarrSettings.connectionAddress,
                apiKey = sonarrSettings.apiKey
            ),
            transmissionSettingsState = currentsState.transmissionSettingsState.copy(
                connectionAddress = transmissionSettings.connectionAddress,
                username = transmissionSettings.username,
                password = transmissionSettings.password,
            ),
            currentTheme = theme,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = _uiState.value
    )
    private val _errorFlow = MutableSharedFlow<String>(
        replay = 0,
        extraBufferCapacity = 1
    )
    val errorFlow = _errorFlow.asSharedFlow()

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
                    val result = radarrRepository.systemStatus(
                        connectionAddress = event.connectionAddress,
                        apiKey = event.apiKey
                    )

                    if (result == null) {
                        _errorFlow.emit("Error while connecting to Radarr")
                    }

                    _uiState.update {
                        it.copy(
                            radarrSettingsState = it.radarrSettingsState.copy(
                                isTesting = false,
                                isEnabled = result != null
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
                    val result = sonarrRepository.testConnection(
                        connectionAddress = event.connectionAddress,
                        apiKey = event.apiKey
                    )

                    if (result == null) {
                        _errorFlow.emit("Error while connecting to Sonarr")
                    }

                    _uiState.update {
                        it.copy(
                            sonarrSettingsState = it.sonarrSettingsState.copy(
                                isTesting = false,
                                isEnabled = result != null
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

            is SettingsEvent.TestTransmissionConnection -> {
                job?.cancel()
                job = viewModelScope.launch {
                    _uiState.update {
                        it.copy(
                            transmissionSettingsState = it.transmissionSettingsState.copy(
                                isTesting = true,
                                isEnabled = false
                            )
                        )
                    }
                    val result = downloadsRepository.testConnection(
                        connectionAddress = event.connectionAddress,
                        username = event.username,
                        password = event.password
                    )

                    if (result == null) {
                        _errorFlow.emit("Error while connecting to Transmission")
                    }

                    _uiState.update {
                        it.copy(
                            transmissionSettingsState = it.transmissionSettingsState.copy(
                                isTesting = false,
                                isEnabled = result != null
                            )
                        )
                    }
                }
            }

            is SettingsEvent.SaveTransmissionSettings -> {
                viewModelScope.launch {
                    preferencesRepository.setTransmissionSettings(
                        connectionAddress = event.connectionAddress,
                        username = event.username,
                        password = event.password
                    )
                    _uiState.update {
                        it.copy(
                            transmissionSettingsState = it.transmissionSettingsState.copy(
                                isEnabled = false
                            )
                        )
                    }
                    event.onDismiss()
                }
            }

            SettingsEvent.ResetTransmissionStatus -> {
                job?.cancel()
                _uiState.update {
                    it.copy(
                        transmissionSettingsState = it.transmissionSettingsState.copy(
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