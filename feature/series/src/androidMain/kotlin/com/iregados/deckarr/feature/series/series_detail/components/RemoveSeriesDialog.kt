package com.iregados.deckarr.feature.series.series_detail.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.unit.dp

@Composable
fun RemoveSeriesDialog(
    showDialog: Boolean,
    onRemoveSeries: (addToExclusionList: Boolean, deleteFiles: Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    if (!showDialog) return

    var addToExclusionList by remember { mutableStateOf(false) }
    var deleteFiles by remember { mutableStateOf(true) }

    val iS1 = remember { MutableInteractionSource() }
    val iS2 = remember { MutableInteractionSource() }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Remove Series") },
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
                        onCheckedChange = {},
                        interactionSource = iS1
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = iS2,
                            indication = null
                        ) {
                            addToExclusionList = !addToExclusionList
                        },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Add to Exclusion List",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Switch(
                        checked = addToExclusionList,
                        onCheckedChange = {},
                        interactionSource = iS2
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                onRemoveSeries(
                    addToExclusionList,
                    deleteFiles
                )
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