package com.iregados.deckarr.core.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iregados.deckarr.core.domain.PreferencesRepository
import com.iregados.deckarr.core.util.dto.ThemeOption
import org.koin.compose.koinInject

@Composable
actual fun DeckarrTheme(
    darkSystem: Boolean,
    dynamicColor: Boolean,
    content: @Composable (() -> Unit)
) {
    val preferences: PreferencesRepository = koinInject()
    val theme by preferences.getTheme().collectAsStateWithLifecycle(ThemeOption.System)

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current

            if (
                (theme == ThemeOption.System && darkSystem) ||
                theme == ThemeOption.Dark
            ) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        (theme == ThemeOption.System && darkSystem) ||
                theme == ThemeOption.Dark -> darkScheme

        else -> lightScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}

actual val ColorScheme.successGreen: Color
    @Composable get() {
        val preferences: PreferencesRepository = koinInject()
        val theme by preferences.getTheme().collectAsStateWithLifecycle(ThemeOption.System)

        return when (theme) {
            ThemeOption.Light -> successGreenLight
            ThemeOption.Dark -> successGreenDark
            else -> if (isSystemInDarkTheme()) successGreenDark else successGreenLight
        }
    }
actual val ColorScheme.warningYellow: Color
    @Composable get() {
        val preferences: PreferencesRepository = koinInject()
        val theme by preferences.getTheme().collectAsStateWithLifecycle(ThemeOption.System)

        return when (theme) {
            ThemeOption.Light -> warningYellowLight
            ThemeOption.Dark -> warningYellowDark
            else -> if (isSystemInDarkTheme()) warningYellowDark else warningYellowLight
        }
    }