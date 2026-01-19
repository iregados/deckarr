package com.iregados.deckarr.feature.downloads.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.iregados.deckarr.feature.downloads.DownloadsScreen
import com.iregados.deckarr.feature.downloads.DownloadsViewModel
import com.iregados.deckarr.feature.downloads.navigation.keys.DownloadsKey

fun EntryProviderScope<NavKey>.downloadsEntry(
    downloadsViewModel: DownloadsViewModel
) {
    entry<DownloadsKey> {
        DownloadsScreen(downloadsViewModel)
    }
}
