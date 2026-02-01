package com.iregados.deckarr.feature.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iregados.deckarr.core.domain.util.TorrentClientOption
import com.iregados.deckarr.core.presentation.components.rememberErrorFlowHandler
import com.iregados.deckarr.core.util.dto.RadioOption
import com.iregados.deckarr.feature.settings.SettingsEvent
import com.iregados.deckarr.feature.settings.SettingsViewModel

@Composable
fun DownloadsConfigurationComponent(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    settingsViewModel: SettingsViewModel
) {
    val uiState by settingsViewModel.uiState.collectAsStateWithLifecycle()
    val torrentClientOptionList = remember {
        listOf(
            RadioOption(label = "QBitTorrent", value = TorrentClientOption.QBitTorrent),
            RadioOption(label = "Transmission", value = TorrentClientOption.Transmission)
        )
    }


    var selectedClient by remember(uiState.downloadsSettingsState.selectedClient) {
        mutableStateOf(
            uiState.downloadsSettingsState.selectedClient
        )
    }
    var connectionAddress by remember(uiState.downloadsSettingsState.connectionAddress) {
        mutableStateOf(
            uiState.downloadsSettingsState.connectionAddress
        )
    }
    var username by remember(uiState.downloadsSettingsState.username) {
        mutableStateOf(
            uiState.downloadsSettingsState.username
        )
    }
    var password by remember(uiState.downloadsSettingsState.password) {
        mutableStateOf(
            uiState.downloadsSettingsState.password
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
                text = "Torrent Configuration",
                style = MaterialTheme.typography.titleLarge
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                torrentClientOptionList.forEach {
                    val interactionSource = remember { MutableInteractionSource() }
                    Row(
                        modifier = Modifier
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                selectedClient = it.value
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            modifier = Modifier,
                            selected = it.value == selectedClient,
                            onClick = { selectedClient = it.value },
                            interactionSource = interactionSource
                        )
                        Text(
                            modifier = Modifier,
                            text = it.label,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = connectionAddress,
                onValueChange = {
                    connectionAddress = it
                    settingsViewModel.onEvent(
                        SettingsEvent.ResetDownloadsStatus
                    )
                },
                label = { Text("Connection Address") },
                singleLine = true
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = username,
                onValueChange = {
                    username = it
                    settingsViewModel.onEvent(
                        SettingsEvent.ResetDownloadsStatus
                    )
                },
                label = { Text("Username") },
                singleLine = true
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = password,
                onValueChange = {
                    password = it
                    settingsViewModel.onEvent(
                        SettingsEvent.ResetDownloadsStatus
                    )
                },
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
            )

            Button(
                modifier = Modifier
                    .height(48.dp)
                    .width(120.dp)
                    .align(Alignment.End),
                onClick = {
                    if (uiState.downloadsSettingsState.isEnabled) {
                        settingsViewModel.onEvent(
                            SettingsEvent.SaveDownloadsSettings(
                                selectedClient = selectedClient,
                                connectionAddress = connectionAddress,
                                username = username,
                                password = password,
                                onDismiss = onDismiss
                            )
                        )
                    } else {
                        settingsViewModel.onEvent(
                            SettingsEvent.TestDownloadsConnection(
                                selectedClient = selectedClient,
                                connectionAddress = connectionAddress,
                                username = username,
                                password = password
                            )
                        )
                    }
                }
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    if (uiState.downloadsSettingsState.isTesting) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            if (uiState.downloadsSettingsState.isEnabled) "Save" else "Test"
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