package com.iregados.deckarr.feature.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iregados.deckarr.core.presentation.components.rememberErrorFlowHandler
import com.iregados.deckarr.feature.settings.SettingsEvent
import com.iregados.deckarr.feature.settings.SettingsViewModel

@Composable
fun SonarrConfigurationComponent(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    settingsViewModel: SettingsViewModel
) {
    val uiState by settingsViewModel.uiState.collectAsStateWithLifecycle()

    var connectionAddress by remember(uiState.sonarrSettingsState.connectionAddress) {
        mutableStateOf(
            uiState.sonarrSettingsState.connectionAddress
        )
    }
    var apiKey by remember(uiState.sonarrSettingsState.apiKey) {
        mutableStateOf(
            uiState.sonarrSettingsState.apiKey
        )
    }
    var errorMessage by rememberErrorFlowHandler(
        errorFlow = settingsViewModel.errorFlow
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = "Sonarr Configuration",
                style = MaterialTheme.typography.titleLarge
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = connectionAddress,
                onValueChange = {
                    connectionAddress = it
                    settingsViewModel.onEvent(
                        SettingsEvent.ResetSonarrStatus
                    )
                },
                label = { Text("Connection Address") },
                singleLine = true
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = apiKey,
                onValueChange = {
                    apiKey = it
                    settingsViewModel.onEvent(
                        SettingsEvent.ResetSonarrStatus
                    )
                },
                label = { Text("API Key") },
                singleLine = true
            )

            Button(
                modifier = Modifier
                    .height(48.dp)
                    .width(120.dp)
                    .align(Alignment.End),
                onClick = {
                    if (uiState.sonarrSettingsState.isEnabled) {
                        settingsViewModel.onEvent(
                            SettingsEvent.SaveSonarrSettings(
                                connectionAddress = connectionAddress,
                                apiKey = apiKey,
                                onDismiss = onDismiss
                            )
                        )
                    } else {
                        settingsViewModel.onEvent(
                            SettingsEvent.TestSonarrConnection(
                                connectionAddress = connectionAddress,
                                apiKey = apiKey
                            )
                        )
                    }
                }
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    if (uiState.sonarrSettingsState.isTesting) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            if (uiState.sonarrSettingsState.isEnabled) "Save" else "Test"
                        )
                    }
                }
            }
        }

        if (errorMessage != null) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(MaterialTheme.colorScheme.errorContainer.copy(alpha = .5f))
                    .clickable {
                        errorMessage = null
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(8.dp),
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}