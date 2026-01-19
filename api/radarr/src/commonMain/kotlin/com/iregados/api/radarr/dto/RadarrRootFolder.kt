package com.iregados.api.radarr.dto

import kotlinx.serialization.Serializable

@Serializable
data class RadarrRootFolder(
    val id: Long,
    val path: String?,
    val accessible: Boolean,
    val freeSpace: Long
)
