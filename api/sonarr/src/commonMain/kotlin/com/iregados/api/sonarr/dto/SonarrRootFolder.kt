package com.iregados.api.sonarr.dto

import kotlinx.serialization.Serializable

@Serializable
data class SonarrRootFolder(
    val id: Long,
    val path: String?,
    val accessible: Boolean,
    val freeSpace: Long
)
