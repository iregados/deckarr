package com.iregados.api.radarr.dto

import kotlinx.serialization.Serializable

@Serializable
data class RadarrImage(
    val coverType: String,
    val url: String?,
    val remoteUrl: String,
)