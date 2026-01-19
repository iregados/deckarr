package com.iregados.api.transmission.dto.internal

import kotlinx.serialization.Serializable

@Serializable
internal data class SessionStatsRequest(
    val method: String = "session-stats"
)