package com.iregados.deckarr.core.util.extension

import com.iregados.deckarr.api.common.dto.TorrentImpl
import com.iregados.deckarr.api.common.interfaces.Torrent
import com.iregados.deckarr.api.common.interfaces.TorrentActivityResponse

fun List<Torrent>.mergeTorrents(update: TorrentActivityResponse): List<Torrent> {
    val updatesById = update.torrents.associateBy { it.id }

    val existing = map { torrent ->
        val updated = updatesById[torrent.id]
        if (updated == null) {
            torrent
        } else {
            TorrentImpl(
                id = torrent.id,
                name = updated.name ?: torrent.name,
                addedDate = updated.addedDate ?: torrent.addedDate,
                status = updated.status ?: torrent.status,
                totalSize = updated.totalSize ?: torrent.totalSize,
                percentDone = updated.percentDone ?: torrent.percentDone,
                isFinished = updated.isFinished ?: torrent.isFinished,
                isStalled = updated.isStalled ?: torrent.isStalled,
                rateDownload = updated.rateDownload ?: torrent.rateDownload,
                rateUpload = updated.rateUpload ?: torrent.rateUpload,
                uploadedEver = updated.uploadedEver ?: torrent.uploadedEver,
                downloadedEver = updated.downloadedEver ?: torrent.downloadedEver,
                eta = updated.eta ?: torrent.eta,
            )
        }
    }

    val added = update.torrents.filter { newTorrent ->
        this.none { it.id == newTorrent.id }
    }

    return (existing + added)
        .filter { torrent -> torrent.id !in update.removed }
        .sortedByDescending { it.addedDate }
}