package com.iregados.deckarr.core.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

// This function is expected to return a LAMBDA that, when called, produces the File.
expect fun createDataStore(): DataStore<Preferences>

internal const val DATA_STORE_FILE_NAME = "prefs.preferences_pb"
