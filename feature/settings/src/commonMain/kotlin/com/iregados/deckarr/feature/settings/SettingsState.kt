package com.iregados.deckarr.feature.settings

import com.iregados.deckarr.core.domain.util.TorrentClientOption
import com.iregados.deckarr.core.util.dto.ThemeOption
import kotlinx.serialization.Serializable

@Serializable
data class SettingsState(
    val isLoading: Boolean = true,
    val currentTheme: String = ThemeOption.System,
    val radarrSettingsState: RadarrSettingsState = RadarrSettingsState(),
    val sonarrSettingsState: SonarrSettingsState = SonarrSettingsState(),
    val downloadsSettingsState: DownloadsSettingsState = DownloadsSettingsState(),
)

@Serializable
data class RadarrSettingsState(
    val connectionAddress: String = "",
    val apiKey: String = "",
    val isEnabled: Boolean = false,
    val isTesting: Boolean = false,
)

@Serializable
data class SonarrSettingsState(
    val connectionAddress: String = "",
    val apiKey: String = "",
    val isEnabled: Boolean = false,
    val isTesting: Boolean = false,
)

@Serializable
data class DownloadsSettingsState(
    val selectedClient: TorrentClientOption = TorrentClientOption.Transmission,
    val connectionAddress: String = "",
    val username: String = "",
    val password: String = "",
    val isEnabled: Boolean = false,
    val isTesting: Boolean = false,
)