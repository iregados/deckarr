package com.iregados.api.sonarr.dto

import kotlinx.serialization.Serializable

@Serializable
data class SonarrImage(
    val coverType: String,
    val url: String?,
    val remoteUrl: String,
)