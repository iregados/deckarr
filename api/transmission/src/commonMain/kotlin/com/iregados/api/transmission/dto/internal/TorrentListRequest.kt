package com.iregados.api.transmission.dto.internal

import kotlinx.serialization.Serializable

@Serializable
internal data class TorrentListRequest(
    val method: String = "torrent-get",
    val arguments: Arguments = Arguments()
) {
    @Serializable
    data class Arguments(
        val format: String = "table",
        val fields: List<String> = listOf(
            "id",
            "name",
            "addedDate",
            "status",
            "totalSize",
            "percentDone",
            "error",
            "errorString",
            "isFinished",
            "isStalled",
            "rateDownload",
            "rateUpload",
            "uploadedEver",
            "downloadedEver",
            "eta",
            "queuePosition",
            "seedRatioMode",
            "seedRatioLimit",
            "peersConnected",
            "peersGettingFromUs",
            "peersSendingToUs",
            "trackers",
            "trackerStats",
        )
    )
}