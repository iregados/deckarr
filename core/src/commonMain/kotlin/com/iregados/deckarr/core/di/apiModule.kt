package com.iregados.deckarr.core.di

import com.iregados.api.common.interfaces.TorrentClientApi
import com.iregados.api.qbittorrent.QBitTorrentApi
import com.iregados.api.radarr.RadarrApi
import com.iregados.api.sonarr.SonarrApi
import com.iregados.api.transmission.TransmissionApi
import com.iregados.deckarr.core.domain.PreferencesRepository
import com.iregados.deckarr.core.domain.dto.DownloadsSettings
import com.iregados.deckarr.core.domain.util.TorrentClientOption
import io.ktor.http.URLProtocol
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.runBlocking
import org.koin.dsl.module

val apiModule = module {
    single { CoroutineScope(SupervisorJob() + Dispatchers.IO) }

    single {
        val preferencesRepository: PreferencesRepository = get()
        val moduleScope: CoroutineScope = get()

        val apiFlow = preferencesRepository.getRadarrSettings()

        val initialSettings = runBlocking { apiFlow.first() }
        val radarrApi = RadarrApi(
            protocol = if (initialSettings.connectionAddress.startsWith("https")) URLProtocol.HTTPS else URLProtocol.HTTP,
            host = initialSettings.connectionAddress.substringAfter("://"),
            apiKey = initialSettings.apiKey
        )

        apiFlow
            .drop(1)
            .distinctUntilChanged()
            .onEach {
                radarrApi.configure(
                    if (it.connectionAddress.startsWith("https")) URLProtocol.HTTPS else URLProtocol.HTTP,
                    it.connectionAddress.substringAfter("://"),
                    apiKey = it.apiKey
                )
            }.launchIn(moduleScope)

        radarrApi
    }

    single {
        val preferencesRepository: PreferencesRepository = get()
        val moduleScope: CoroutineScope = get()

        val apiFlow = preferencesRepository.getSonarrSettings()

        val initialSettings = runBlocking { apiFlow.first() }
        val sonarrApi = SonarrApi(
            protocol = if (initialSettings.connectionAddress.startsWith("https")) URLProtocol.HTTPS else URLProtocol.HTTP,
            host = initialSettings.connectionAddress.substringAfter("://"),
            apiKey = initialSettings.apiKey
        )

        apiFlow
            .drop(1)
            .distinctUntilChanged()
            .onEach {
                sonarrApi.configure(
                    if (it.connectionAddress.startsWith("https")) URLProtocol.HTTPS else URLProtocol.HTTP,
                    it.connectionAddress.substringAfter("://"),
                    apiKey = it.apiKey
                )
            }.launchIn(moduleScope)

        sonarrApi
    }

    single {
        val preferencesRepository: PreferencesRepository = get()
        val moduleScope: CoroutineScope = get()

        val apiFlow = preferencesRepository.getDownloadsSettings()

        val initialSettings = runBlocking { apiFlow.first() }
        val torrentClientApiFlow = MutableStateFlow(buildTorrentClient(initialSettings))

        apiFlow
            .drop(1)
            .distinctUntilChanged()
            .onEach { settings ->
                torrentClientApiFlow.update { buildTorrentClient(settings) }
            }.launchIn(moduleScope)

        torrentClientApiFlow.asStateFlow()
    }
}

private fun buildTorrentClient(
    settings: DownloadsSettings
): TorrentClientApi {
    return when (settings.selectedClient) {
        TorrentClientOption.Transmission -> TransmissionApi(
            protocol = if (settings.connectionAddress.startsWith("https")) URLProtocol.HTTPS else URLProtocol.HTTP,
            host = settings.connectionAddress.substringAfter("://"),
            username = settings.username,
            password = settings.password
        )

        TorrentClientOption.QBitTorrent -> QBitTorrentApi(
            protocol = if (settings.connectionAddress.startsWith("https")) URLProtocol.HTTPS else URLProtocol.HTTP,
            host = settings.connectionAddress.substringAfter("://"),
            username = settings.username,
            password = settings.password
        )
    }
}
