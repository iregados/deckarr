package com.iregados.deckarr.core.domain

import com.iregados.deckarr.core.data.dao.DataStoreDao
import com.iregados.deckarr.core.domain.dto.DownloadsSettings
import com.iregados.deckarr.core.domain.dto.RadarrSettings
import com.iregados.deckarr.core.domain.dto.SonarrSettings
import com.iregados.deckarr.core.domain.util.APP_THEME
import com.iregados.deckarr.core.domain.util.DOWNLOADS_CONNECTION_ADDRESS
import com.iregados.deckarr.core.domain.util.DOWNLOADS_PASSWORD
import com.iregados.deckarr.core.domain.util.DOWNLOADS_SELECTED_CLIENT
import com.iregados.deckarr.core.domain.util.DOWNLOADS_USERNAME
import com.iregados.deckarr.core.domain.util.RADARR_API_KEY
import com.iregados.deckarr.core.domain.util.RADARR_CONNECTION_ADDRESS
import com.iregados.deckarr.core.domain.util.RADARR_QUALITY_PROFILE_ID
import com.iregados.deckarr.core.domain.util.RADARR_ROOT_FOLDER_PATH
import com.iregados.deckarr.core.domain.util.SONARR_API_KEY
import com.iregados.deckarr.core.domain.util.SONARR_CONNECTION_ADDRESS
import com.iregados.deckarr.core.domain.util.SONARR_QUALITY_PROFILE_ID
import com.iregados.deckarr.core.domain.util.SONARR_ROOT_FOLDER_PATH
import com.iregados.deckarr.core.domain.util.TorrentClientOption
import com.iregados.deckarr.core.util.dto.ThemeOption
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

class PreferencesRepository(
    private val dataStoreDao: DataStoreDao
) {
    private val json = Json

    suspend fun setRadarrSettings(
        connectionAddress: String,
        apiKey: String
    ) {
        dataStoreDao.saveString(RADARR_CONNECTION_ADDRESS, connectionAddress)
        dataStoreDao.saveString(RADARR_API_KEY, apiKey)
    }

    suspend fun setSonarrSettings(
        connectionAddress: String,
        apiKey: String
    ) {
        dataStoreDao.saveString(SONARR_CONNECTION_ADDRESS, connectionAddress)
        dataStoreDao.saveString(SONARR_API_KEY, apiKey)
    }

    suspend fun setDownloadsSettings(
        selectedClient: TorrentClientOption,
        connectionAddress: String,
        username: String,
        password: String
    ) {
        dataStoreDao.saveString(DOWNLOADS_SELECTED_CLIENT, json.encodeToString(selectedClient))
        dataStoreDao.saveString(DOWNLOADS_CONNECTION_ADDRESS, connectionAddress)
        dataStoreDao.saveString(DOWNLOADS_USERNAME, username)
        dataStoreDao.saveString(DOWNLOADS_PASSWORD, password)
    }

    suspend fun setRadarrRootFolderPath(
        rootFolderPath: String
    ) {
        dataStoreDao.saveString(RADARR_ROOT_FOLDER_PATH, rootFolderPath)
    }

    suspend fun setRadarrQualityProfileId(
        qualityProfileId: Long
    ) {
        dataStoreDao.saveLong(RADARR_QUALITY_PROFILE_ID, qualityProfileId)
    }

    suspend fun setSonarrRootFolderPath(
        rootFolderPath: String
    ) {
        dataStoreDao.saveString(SONARR_ROOT_FOLDER_PATH, rootFolderPath)
    }

    suspend fun setSonarrQualityProfileId(
        qualityProfileId: Long
    ) {
        dataStoreDao.saveLong(SONARR_QUALITY_PROFILE_ID, qualityProfileId)
    }

    suspend fun setTheme(
        theme: String
    ) {
        dataStoreDao.saveString(APP_THEME, theme)
    }

    fun getRadarrSettings(): Flow<RadarrSettings> {
        return combine(
            dataStoreDao.getString(RADARR_CONNECTION_ADDRESS),
            dataStoreDao.getString(RADARR_API_KEY)
        ) { connectionAddress, apiKey ->
            RadarrSettings(
                connectionAddress = connectionAddress ?: "",
                apiKey = apiKey ?: ""
            )
        }
    }

    fun getSonarrSettings(): Flow<SonarrSettings> {
        return combine(
            dataStoreDao.getString(SONARR_CONNECTION_ADDRESS),
            dataStoreDao.getString(SONARR_API_KEY)
        ) { connectionAddress, apiKey ->
            SonarrSettings(
                connectionAddress = connectionAddress ?: "",
                apiKey = apiKey ?: ""
            )
        }
    }

    fun getDownloadsSettings(): Flow<DownloadsSettings> {
        return combine(
            dataStoreDao.getString(DOWNLOADS_SELECTED_CLIENT),
            dataStoreDao.getString(DOWNLOADS_CONNECTION_ADDRESS),
            dataStoreDao.getString(DOWNLOADS_USERNAME),
            dataStoreDao.getString(DOWNLOADS_PASSWORD)
        ) { selectedClient, connectionAddress, username, password ->
            DownloadsSettings(
                selectedClient = if (selectedClient == null)
                    TorrentClientOption.Transmission else
                    json.decodeFromString(selectedClient),
                connectionAddress = connectionAddress ?: "",
                username = username ?: "",
                password = password ?: ""
            )
        }
    }

    fun getRadarrRootFolderPath(): Flow<String?> {
        return dataStoreDao.getString(RADARR_ROOT_FOLDER_PATH)
    }

    fun getRadarrQualityProfileId(): Flow<Long?> {
        return dataStoreDao.getLong(RADARR_QUALITY_PROFILE_ID)
    }

    fun getSonarrRootFolderPath(): Flow<String?> {
        return dataStoreDao.getString(SONARR_ROOT_FOLDER_PATH)
    }

    fun getSonarrQualityProfileId(): Flow<Long?> {
        return dataStoreDao.getLong(SONARR_QUALITY_PROFILE_ID)
    }

    fun getTheme(): Flow<String> {
        return dataStoreDao.getString(APP_THEME).map {
            it ?: ThemeOption.System
        }
    }
}