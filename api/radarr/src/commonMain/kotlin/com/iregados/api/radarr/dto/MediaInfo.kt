package com.iregados.api.radarr.dto

import kotlinx.serialization.Serializable

@Serializable
data class MediaInfo(
    val audioBitrate: Long,
    val audioCodec: String?,
    val audioLanguages: String,
    val videoBitrate: Long,
    val videoCodec: String?,
    val videoDynamicRange: String,
    val videoDynamicRangeType: String,
    val resolution: String,
    val runTime: String,
    val subtitles: String,
)