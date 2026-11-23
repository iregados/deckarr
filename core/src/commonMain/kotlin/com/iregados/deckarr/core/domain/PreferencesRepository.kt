package com.iregados.deckarr.core.domain

import com.iregados.deckarr.core.domain.dto.DownloadsSettings
import com.iregados.deckarr.core.domain.dto.RadarrSettings
import com.iregados.deckarr.core.domain.dto.SonarrSettings
import com.iregados.deckarr.core.domain.util.TorrentClientOption
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    suspend fun setRadarrSettings(
        connectionAddress: String,
        apiKey: String
    )

    suspend fun setSonarrSettings(
        connectionAddress: String,
        apiKey: String
    )

    suspend fun setDownloadsSettings(
        selectedClient: TorrentClientOption,
        connectionAddress: String,
        username: String,
        password: String
    )

    suspend fun setRadarrRootFolderPath(rootFolderPath: String)

    suspend fun setRadarrQualityProfileId(qualityProfileId: Long)

    suspend fun setSonarrRootFolderPath(rootFolderPath: String)

    suspend fun setSonarrQualityProfileId(qualityProfileId: Long)

    suspend fun setTheme(theme: String)

    fun getRadarrSettings(): Flow<RadarrSettings>

    fun getSonarrSettings(): Flow<SonarrSettings>

    fun getDownloadsSettings(): Flow<DownloadsSettings>

    fun getRadarrRootFolderPath(): Flow<String?>

    fun getRadarrQualityProfileId(): Flow<Long?>

    fun getSonarrRootFolderPath(): Flow<String?>

    fun getSonarrQualityProfileId(): Flow<Long?>

    fun getTheme(): Flow<String>
}