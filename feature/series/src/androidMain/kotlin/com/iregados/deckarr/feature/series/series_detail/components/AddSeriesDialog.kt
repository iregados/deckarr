package com.iregados.deckarr.feature.series.series_detail.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.iregados.deckarr.feature.series.series_detail.SeriesDetailState

@Composable
fun AddSeriesDialog(
    showDialog: Boolean,
    uiState: SeriesDetailState,
    onAddSeries: (qualityProfileId: Long, rootFolderPath: String, shouldMonitor: Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    if (!showDialog) return

    var selectedProfileId by remember { mutableStateOf(uiState.selectedQualityProfileId) }
    var selectedFolderPath by remember { mutableStateOf(uiState.selectedRootFolderPath) }

    var expandedSelectRootFolder by remember { mutableStateOf(false) }
    var expandedSelectQualityProfile by remember { mutableStateOf(false) }
    var shouldMonitor by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Series") },
        text = {

            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            shouldMonitor = !shouldMonitor
                        },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Monitor",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Switch(
                        checked = shouldMonitor,
                        onCheckedChange = { shouldMonitor = !shouldMonitor },
                        interactionSource = interactionSource
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Select Quality Profile",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable {
                                expandedSelectQualityProfile = true
                            },
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(start = 16.dp),
                            text = uiState.sonarrQualityProfiles!!.first { it.id == selectedProfileId }.name
                                ?: uiState.sonarrQualityProfiles.first { it.id == selectedProfileId }.id.toString()
                        )
                    }
                    DropdownMenu(
                        expanded = expandedSelectQualityProfile,
                        onDismissRequest = { expandedSelectQualityProfile = false }
                    ) {
                        uiState.sonarrQualityProfiles!!.forEach { profile ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedProfileId = profile.id
                                    expandedSelectQualityProfile = false
                                },
                                text = {
                                    Text(profile.name ?: profile.id.toString())
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Select Root Folder",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable {
                                expandedSelectRootFolder = true
                            },
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(start = 16.dp),
                            text = uiState.sonarrRootFolders!!.first { it.path == selectedFolderPath }.path
                                ?: uiState.sonarrRootFolders.first { it.path == selectedFolderPath }.id.toString()
                        )
                    }
                    DropdownMenu(
                        expanded = expandedSelectRootFolder,
                        onDismissRequest = { expandedSelectRootFolder = false }
                    ) {
                        uiState.sonarrRootFolders!!.forEach { folder ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedFolderPath = folder.path
                                    expandedSelectRootFolder = false
                                },
                                text = { Text(folder.path ?: folder.id.toString()) }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                onAddSeries(
                    selectedProfileId!!,
                    selectedFolderPath!!,
                    shouldMonitor
                )
            }) {
                Text("Add")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}