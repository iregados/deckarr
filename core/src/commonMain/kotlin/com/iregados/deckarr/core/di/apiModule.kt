package com.iregados.deckarr.core.di

import com.iregados.deckarr.api.common.interfaces.TorrentClientApi
import com.iregados.deckarr.api.common.util.ConnectionAddress
import com.iregados.deckarr.api.radarr.RadarrApi
import com.iregados.deckarr.api.sonarr.SonarrApi
import com.iregados.deckarr.api.tmdb.TmdbApi
import com.iregados.deckarr.api.tmdb.dto.TmdbApiConfig
import com.iregados.deckarr.core.domain.PreferencesRepository
import com.iregados.deckarr.core.domain.TorrentClientFactory
import io.ktor.http.URLProtocol
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.core.qualifier.named
import org.koin.dsl.module

val apiModule = module {
    single(named("AppScope")) { CoroutineScope(SupervisorJob() + Dispatchers.IO) }

    single {
        val preferencesRepository: PreferencesRepository = get()
        val moduleScope: CoroutineScope = get(named("AppScope"))

        val apiFlow = preferencesRepository.getRadarrSettings()

        // Placeholder client; configure() runs on the first settings emission.
        val radarrApi = RadarrApi(
            protocol = URLProtocol.HTTP,
            host = "",
            apiKey = "",
            basePath = ""
        )

        apiFlow
            .distinctUntilChanged()
            .onEach {
                val address = ConnectionAddress.parse(it.connectionAddress)
                radarrApi.configure(
                    protocol = address.protocol,
                    host = address.host,
                    apiKey = it.apiKey,
                    basePath = address.path
                )
            }.launchIn(moduleScope)

        radarrApi
    }

    single {
        val preferencesRepository: PreferencesRepository = get()
        val moduleScope: CoroutineScope = get(named("AppScope"))

        val apiFlow = preferencesRepository.getSonarrSettings()

        // Placeholder client; configure() runs on the first settings emission.
        val sonarrApi = SonarrApi(
            protocol = URLProtocol.HTTP,
            host = "",
            apiKey = "",
            basePath = ""
        )

        apiFlow
            .distinctUntilChanged()
            .onEach {
                val address = ConnectionAddress.parse(it.connectionAddress)
                sonarrApi.configure(
                    protocol = address.protocol,
                    host = address.host,
                    apiKey = it.apiKey,
                    basePath = address.path
                )
            }.launchIn(moduleScope)

        sonarrApi
    }

    single {
        TmdbApi(
            config = TmdbApiConfig(
                accessToken = getProperty("tmdb.accessToken", "")
            )
        )
    }

    single {
        val preferencesRepository: PreferencesRepository = get()
        val moduleScope: CoroutineScope = get(named("AppScope"))
        val torrentClientFactory: TorrentClientFactory = get()

        val apiFlow = preferencesRepository.getDownloadsSettings()

        val torrentClientApiFlow = MutableStateFlow<TorrentClientApi?>(null)

        apiFlow
            .distinctUntilChanged()
            .onEach { settings ->
                val newClient = torrentClientFactory.create(settings)
                val previousClient = torrentClientApiFlow.getAndUpdate { newClient }
                previousClient?.close()
            }.launchIn(moduleScope)

        torrentClientApiFlow.asStateFlow()
    }
}