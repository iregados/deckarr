package com.iregados.deckarr.core.data.dao


import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.iregados.deckarr.core.data.createDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreDao() {
    private var dataStore: DataStore<Preferences> = createDataStore()

    suspend fun saveString(key: String, data: String) {
        val prefKey = stringPreferencesKey(key)
        dataStore.edit { preferences ->
            preferences[prefKey] = data
        }
    }

    suspend fun saveBoolean(key: String, data: Boolean) {
        val prefKey = booleanPreferencesKey(key)
        dataStore.edit { preferences ->
            preferences[prefKey] = data
        }
    }

    suspend fun saveLong(key: String, data: Long) {
        val prefKey = longPreferencesKey(key)
        dataStore.edit { preferences ->
            preferences[prefKey] = data
        }
    }


    fun getString(key: String): Flow<String?> {
        val prefKey = stringPreferencesKey(key)

        return dataStore.data
            .map { preferences ->
                preferences[prefKey]
            }
    }

    fun getBoolean(key: String): Flow<Boolean?> {
        val prefKey = booleanPreferencesKey(key)
        return dataStore.data
            .map { preferences ->
                preferences[prefKey]
            }
    }

    fun getLong(key: String): Flow<Long?> {
        val prefKey = longPreferencesKey(key)
        return dataStore.data
            .map { preferences ->
                preferences[prefKey]
            }
    }
}