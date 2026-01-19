package com.iregados.api.sonarr.dto

import kotlinx.serialization.Serializable

@Serializable
data class AddOptions(
    val monitor: String,
    val searchForCutoffUnmetEpisodes: Boolean,
    val searchForMissingEpisodes: Boolean
)