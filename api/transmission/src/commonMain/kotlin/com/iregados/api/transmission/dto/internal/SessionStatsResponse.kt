package com.iregados.api.transmission.dto.internal

import com.iregados.api.transmission.dto.SessionStats
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class SessionStatsResponse(
    @SerialName("arguments") val arguments: SessionStats,
    @SerialName("result") val result: String
)