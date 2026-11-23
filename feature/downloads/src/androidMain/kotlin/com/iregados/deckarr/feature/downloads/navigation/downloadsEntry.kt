package com.iregados.deckarr.feature.downloads.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.iregados.deckarr.feature.downloads.DownloadsScreen
import com.iregados.deckarr.feature.downloads.keys.DownloadsKey

fun EntryProviderScope<NavKey>.downloadsEntry(
    metadata: Map<String, Any> = emptyMap(),
) {
    entry<DownloadsKey>(metadata = metadata) {
        DownloadsScreen()
    }
}
