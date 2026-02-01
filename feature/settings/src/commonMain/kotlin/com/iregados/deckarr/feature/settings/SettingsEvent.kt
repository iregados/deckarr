package com.iregados.deckarr.feature.settings

import com.iregados.deckarr.core.domain.util.TorrentClientOption

sealed class SettingsEvent() {
    data class TestRadarrConnection(
        val connectionAddress: String,
        val apiKey: String
    ) : SettingsEvent()

    data class SaveRadarrSettings(
        val connectionAddress: String,
        val apiKey: String,
        val onDismiss: () -> Unit
    ) : SettingsEvent()

    object ResetRadarrStatus : SettingsEvent()


    data class TestSonarrConnection(
        val connectionAddress: String,
        val apiKey: String
    ) : SettingsEvent()

    data class SaveSonarrSettings(
        val connectionAddress: String,
        val apiKey: String,
        val onDismiss: () -> Unit
    ) : SettingsEvent()

    object ResetSonarrStatus : SettingsEvent()

    data class TestDownloadsConnection(
        val selectedClient: TorrentClientOption,
        val connectionAddress: String,
        val username: String,
        val password: String
    ) : SettingsEvent()

    data class SaveDownloadsSettings(
        val selectedClient: TorrentClientOption,
        val connectionAddress: String,
        val username: String,
        val password: String,
        val onDismiss: () -> Unit
    ) : SettingsEvent()

    object ResetDownloadsStatus : SettingsEvent()

    data class SetTheme(
        val theme: String
    ) : SettingsEvent()
}
