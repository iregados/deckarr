package com.iregados.deckarr.feature.downloads.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.iregados.api.common.dto.TorrentStatus
import com.iregados.api.common.interfaces.Torrent
import com.iregados.deckarr.core.theme.warningYellow
import com.iregados.deckarr.core.util.extension.round
import com.iregados.deckarr.core.util.extension.toEtaString
import com.iregados.deckarr.core.util.extension.toFormatedSize
import com.iregados.deckarr.core.util.extension.toFormatedSpeed
import com.iregados.deckarr.core.util.extension.toFormatedStatus
import com.iregados.deckarr.feature.downloads.DownloadsEvent
import com.iregados.deckarr.feature.downloads.DownloadsViewModel

@Composable
fun TorrentItem(
    torrent: Torrent,
    isExpanded: Boolean,
    isUpdating: Boolean,
    onExpand: () -> Unit,
    onRemoveTorrent: (id: String) -> Unit,
    downloadsViewModel: DownloadsViewModel
) {
    val statusColor = when (torrent.status) {
        TorrentStatus.DOWNLOADING -> MaterialTheme.colorScheme.primary
        TorrentStatus.SEEDING -> MaterialTheme.colorScheme.primary
        TorrentStatus.STOPPED -> MaterialTheme.colorScheme.error

        TorrentStatus.QUEUED_TO_DOWNLOAD,
        TorrentStatus.QUEUED_TO_SEED,
        TorrentStatus.QUEUED_TO_VERIFY_LOCAL_DATA,
        TorrentStatus.VERIFYING_LOCAL_DATA -> MaterialTheme.colorScheme.warningYellow

        else -> MaterialTheme.colorScheme.error
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onExpand)
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(statusColor, shape = MaterialTheme.shapes.small)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = torrent.name ?: "Unknown",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Row {
            Text(
                text = torrent.status.toFormatedStatus(),
                style = MaterialTheme.typography.bodySmall,
                color = statusColor
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = torrent.percentDone?.let { percentDone ->
                    when (percentDone) {
                        in 0.0..<0.1 -> (percentDone * 100).round(2)
                        in 0.1..<1.0 -> (percentDone * 100).round(1)
                        else -> (percentDone * 100).round(0)
                    }.toString() + "%"
                } ?: run { "-" },
                style = MaterialTheme.typography.bodySmall,
                color = statusColor
            )
            if (torrent.percentDone != 1f) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = torrent.eta.toEtaString(),
                    style = MaterialTheme.typography.bodySmall,
                    color = statusColor
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { torrent.percentDone ?: 0f },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp),
            color = MaterialTheme.colorScheme.primary,
            trackColor = ProgressIndicatorDefaults.linearTrackColor,
            strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
            drawStopIndicator = {}
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier
                .height(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${
                    ((torrent.totalSize ?: 0L) * (torrent.percentDone ?: 0f)).toLong()
                        .toFormatedSize()
                } / ${torrent.totalSize?.toFormatedSize() ?: "-"}",
                style = MaterialTheme.typography.bodySmall
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier
                .height(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Ratio: ${
                    torrent.uploadedEver
                        ?.toFloat()
                        ?.div(torrent.downloadedEver?.toFloat() ?: 1f)
                        ?.round(2) ?: "-"
                }",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = " (uploaded ${torrent.uploadedEver?.toFormatedSize()})",
                style = MaterialTheme.typography.bodySmall,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.weight(1f))

            torrent.rateDownload?.let {
                if (it != 0) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Download",
                    )
                    Text(
                        text = it.toFormatedSpeed(),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            if ((torrent.rateDownload ?: 0.0) != 0 && (torrent.rateUpload ?: 0.0) != 0) {
                Text(" | ")
            }
            torrent.rateUpload?.let {
                if (it != 0) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropUp,
                        contentDescription = "Upload",
                    )
                    Text(
                        text = it.toFormatedSpeed(),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        AnimatedVisibility(
            visible = isExpanded,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .height(56.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (torrent.status == TorrentStatus.STOPPED) {
                    OutlinedButton(
                        modifier = Modifier
                            .height(48.dp)
                            .width(80.dp),
                        onClick = {
                            if (!isUpdating) {
                                downloadsViewModel.onEvent(
                                    DownloadsEvent.SetResumeTorrent(torrent.id)
                                )
                            }
                        }
                    ) {
                        if (!isUpdating) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Play"
                            )
                        } else {
                            CircularProgressIndicator()
                        }
                    }
                } else {
                    OutlinedButton(
                        modifier = Modifier
                            .height(48.dp)
                            .width(80.dp),
                        onClick = {
                            if (!isUpdating) {
                                downloadsViewModel.onEvent(
                                    DownloadsEvent.SetStopTorrent(torrent.id)
                                )
                            }
                        }
                    ) {
                        if (!isUpdating) {
                            Icon(
                                imageVector = Icons.Default.Pause,
                                contentDescription = "Pause"
                            )
                        } else {
                            CircularProgressIndicator()
                        }
                    }
                }
                OutlinedButton(
                    modifier = Modifier
                        .height(48.dp)
                        .width(80.dp),
                    onClick = {
                        onRemoveTorrent(torrent.id)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Remove",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}