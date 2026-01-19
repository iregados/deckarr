package com.iregados.api.sonarr.dto

import kotlinx.serialization.Serializable

@Serializable
data class SonarrQualityProfile(
    val id: Long,
    val name: String?,
)
