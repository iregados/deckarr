package com.iregados.deckarr.core.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object AndroidDataStoreFileProducer : KoinComponent {
    private val context: Context by inject()

    fun getPreferencesFilePath(): String {
        return context.filesDir.resolve(DATA_STORE_FILE_NAME).absolutePath
    }
}

actual fun createDataStore(): DataStore<Preferences> {
    return PreferenceDataStoreFactory.createWithPath(
        produceFile = { AndroidDataStoreFileProducer.getPreferencesFilePath().toPath() }
    )
}


