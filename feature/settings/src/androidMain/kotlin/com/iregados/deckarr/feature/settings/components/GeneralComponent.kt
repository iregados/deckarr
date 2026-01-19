package com.iregados.deckarr.feature.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iregados.deckarr.feature.settings.SettingsEvent
import com.iregados.deckarr.feature.settings.SettingsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun GeneralComponent(
    modifier: Modifier = Modifier,
    settingsViewModel: SettingsViewModel = koinViewModel()
) {
    val uiState by settingsViewModel.uiState.collectAsStateWithLifecycle()
    var showDialog by remember { mutableStateOf(false) }

    Text(
        modifier = Modifier
            .height(56.dp)
            .fillMaxWidth()
            .padding(16.dp)
            .padding(start = 16.dp),
        text = "General",
        style = MaterialTheme.typography.titleMedium
    )

    Card(
        modifier = modifier
            .padding(horizontal = 16.dp)
    ) {
        Column(
            modifier = Modifier
        ) {
            Row(
                modifier = Modifier
                    .height(56.dp)
                    .fillMaxWidth()
                    .clickable {
                        showDialog = true
                    }
                    .padding(start = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Theme",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

        }
    }

    ThemeSelectionComponent(
        showDialog = showDialog,
        currentTheme = uiState.currentTheme,
        onThemeSelected = {
            settingsViewModel.onEvent(SettingsEvent.SetTheme(it))
            showDialog = false
        },
        onDismiss = { showDialog = false }
    )
}