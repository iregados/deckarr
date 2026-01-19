package com.iregados.api.transmission.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Torrent(
    val id: Int,
    val name: String?,
    @SerialName("added-date") val addedDate: Long? = null,
    val status: Int,
    @SerialName("total-size") val totalSize: Long,
    @SerialName("percent-done") val percentDone: Float?,
    val error: Int? = null,
    @SerialName("error-string") val errorString: String? = null,
    @SerialName("is-finished") val isFinished: Boolean? = null,
    @SerialName("is-stalled") val isStalled: Boolean? = null,
    @SerialName("rate-download") val rateDownload: Int? = null,
    @SerialName("rate-upload") val rateUpload: Int? = null,
    @SerialName("uploaded-ever") val uploadedEver: Long? = null,
    @SerialName("downloaded-ever") val downloadedEver: Long? = null,
    val eta: Int? = null,
    @SerialName("queue-position") val queuePosition: Int? = null,
    @SerialName("seed-ratio-mode") val seedRatioMode: Int? = null,
    @SerialName("seed-ratio-limit") val seedRatioLimit: Double? = null,
    @SerialName("peers-connected") val peersConnected: Int? = null,
    @SerialName("peers-getting-from-us") val peersGettingFromUs: Int? = null,
    @SerialName("peers-sending-to-us") val peersSendingToUs: Int? = null,
    val trackers: List<Tracker>? = null,
    @SerialName("tracker-stats") val trackerStats: List<TrackerStats>? = null,
    @SerialName("activity-date") val activityDate: Long? = null,
)