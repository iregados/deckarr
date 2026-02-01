package com.iregados.deckarr.feature.downloads.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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

@Composable
fun RemoveTorrentDialog(
    showDialog: Boolean,
    onRemoveTorrent: (deleteFiles: Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    if (!showDialog) return

    var deleteFiles by remember { mutableStateOf(true) }
    val iS1 = remember { MutableInteractionSource() }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Remove Torrent") },
        text = {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = iS1,
                            indication = null
                        ) {
                            deleteFiles = !deleteFiles
                        },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Delete Files",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Switch(
                        checked = deleteFiles,
                        onCheckedChange = {
                            deleteFiles = !deleteFiles
                        },
                        interactionSource = iS1
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                onRemoveTorrent(
                    deleteFiles
                )
                onDismiss()
            }) {
                Text("Remove")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}