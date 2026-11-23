package com.iregados.deckarr.api.radarr.dto

import kotlinx.serialization.Serializable

@Serializable
data class RadarrQualityProfile(
    val id: Long,
    val name: String?,
)
