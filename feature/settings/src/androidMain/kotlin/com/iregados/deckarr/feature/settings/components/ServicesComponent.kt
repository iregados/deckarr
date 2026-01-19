package com.iregados.deckarr.feature.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.iregados.deckarr.feature.settings.SettingsViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun ServicesComponent(
    modifier: Modifier = Modifier,
    settingsViewModel: SettingsViewModel = koinViewModel()
) {
    val radarrBottomSheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.Hidden,
        skipHiddenState = false
    )
    val sonarrBottomSheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.Hidden,
        skipHiddenState = false
    )
    val transmissionBottomSheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.Hidden,
        skipHiddenState = false
    )
    val coroutineScope = rememberCoroutineScope()

    if (radarrBottomSheetState.isVisible) {
        ModalBottomSheet(
            sheetState = radarrBottomSheetState,
            onDismissRequest = {
                coroutineScope.launch { radarrBottomSheetState.hide() }
            }
        ) {
            RadarrConfigurationComponent(
                onDismiss = {
                    coroutineScope.launch { radarrBottomSheetState.hide() }
                },
                settingsViewModel = settingsViewModel
            )
        }
    }

    if (sonarrBottomSheetState.isVisible) {
        ModalBottomSheet(
            sheetState = sonarrBottomSheetState,
            onDismissRequest = {
                coroutineScope.launch { sonarrBottomSheetState.hide() }
            }
        ) {
            SonarrConfigurationComponent(
                onDismiss = {
                    coroutineScope.launch { sonarrBottomSheetState.hide() }
                },
                settingsViewModel = settingsViewModel
            )
        }
    }

    if (transmissionBottomSheetState.isVisible) {
        ModalBottomSheet(
            sheetState = transmissionBottomSheetState,
            onDismissRequest = {
                coroutineScope.launch { transmissionBottomSheetState.hide() }
            }
        ) {
            TransmissionConfigurationComponent(
                onDismiss = {
                    coroutineScope.launch { transmissionBottomSheetState.hide() }
                },
                settingsViewModel = settingsViewModel
            )
        }
    }

    Text(
        modifier = Modifier
            .height(56.dp)
            .fillMaxWidth()
            .padding(16.dp)
            .padding(start = 16.dp),
        text = "Services",
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
                        coroutineScope.launch {
                            radarrBottomSheetState.show()
                        }
                    }
                    .padding(start = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Radarr",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Row(
                modifier = Modifier
                    .height(56.dp)
                    .fillMaxWidth()
                    .clickable {
                        coroutineScope.launch {
                            sonarrBottomSheetState.show()
                        }
                    }
                    .padding(start = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Sonarr",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Row(
                modifier = Modifier
                    .height(56.dp)
                    .fillMaxWidth()
                    .clickable {
                        coroutineScope.launch {
                            transmissionBottomSheetState.show()
                        }
                    }
                    .padding(start = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Transmission",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}