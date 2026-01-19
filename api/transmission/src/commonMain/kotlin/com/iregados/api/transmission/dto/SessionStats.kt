package com.iregados.api.transmission.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SessionStats(
    val activeTorrentCount: Long,
    val downloadSpeed: Long?,
    val pausedTorrentCount: Long?,
    val torrentCount: Long?,
    val uploadSpeed: Long?,
    @SerialName("cumulative-stats") val cumulativeStats: CumulativeStats?,
)

@Serializable
data class CumulativeStats(
    val downloadedBytes: Long?,
    val filesAdded: Long?,
    val secondsActive: Long?,
    val sessionCount: Long?,
    val uploadedBytes: Long?,
)
//
//{
//    "arguments" : {
//    "activeTorrentCount" : 528,
//    "cumulative-stats" : {
//        "downloadedBytes" : 1593545933076,
//        "filesAdded" : 93351,
//        "secondsActive" : 159058351,
//        "sessionCount" : 395,
//        "uploadedBytes" : 22073850680452
//    },
//    "downloadSpeed" : 81920,
//    "pausedTorrentCount" : 0,
//    "torrentCount" : 528,
//    "uploadSpeed" : 81985
//},
//    "result" : "success"
//}