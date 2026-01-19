package com.iregados.deckarr.core.di

import com.iregados.api.radarr.RadarrApi
import com.iregados.api.sonarr.SonarrApi
import com.iregados.api.transmission.TransmissionApi
import com.iregados.deckarr.core.domain.PreferencesRepository
import io.ktor.http.URLProtocol
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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

        val apiFlow = preferencesRepository.getTransmissionSettings()

        val initialSettings = runBlocking { apiFlow.first() }
        val transmissionApi = TransmissionApi(
            protocol = if (initialSettings.connectionAddress.startsWith("https")) URLProtocol.HTTPS else URLProtocol.HTTP,
            host = initialSettings.connectionAddress.substringAfter("://"),
            username = initialSettings.username,
            password = initialSettings.password
        )

        apiFlow
            .drop(1)
            .distinctUntilChanged()
            .onEach {
                transmissionApi.configure(
                    if (it.connectionAddress.startsWith("https")) URLProtocol.HTTPS else URLProtocol.HTTP,
                    it.connectionAddress.substringAfter("://"),
                    it.username,
                    it.password
                )
            }.launchIn(moduleScope)

        transmissionApi
    }
}