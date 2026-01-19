package com.iregados.deckarr.feature.settings

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

    data class TestTransmissionConnection(
        val connectionAddress: String,
        val username: String,
        val password: String
    ) : SettingsEvent()

    data class SaveTransmissionSettings(
        val connectionAddress: String,
        val username: String,
        val password: String,
        val onDismiss: () -> Unit
    ) : SettingsEvent()

    object ResetTransmissionStatus : SettingsEvent()

    data class SetTheme(
        val theme: String
    ) : SettingsEvent()
}
