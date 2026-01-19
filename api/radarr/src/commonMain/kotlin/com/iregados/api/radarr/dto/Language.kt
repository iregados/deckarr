package com.iregados.api.radarr.dto

import kotlinx.serialization.Serializable

@Serializable
data class Language(
    val id: Long,
    val name: String
)
