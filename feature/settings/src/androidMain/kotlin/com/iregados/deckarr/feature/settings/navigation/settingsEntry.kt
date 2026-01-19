package com.iregados.deckarr.feature.settings.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.iregados.deckarr.feature.settings.SettingsScreen
import com.iregados.deckarr.feature.settings.navigation.keys.SettingsKey

fun EntryProviderScope<NavKey>.settingsEntry(
    popMainBackStack: () -> Unit,
) {
    entry<SettingsKey> {
        SettingsScreen(
            popMainBackStack = popMainBackStack
        )
    }
}
