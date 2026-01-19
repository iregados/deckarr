package com.iregados.api.radarr.dto

import kotlinx.serialization.Serializable

@Serializable
data class AddOptions(
    val monitor: String,
    val searchForMovie: Boolean,
)