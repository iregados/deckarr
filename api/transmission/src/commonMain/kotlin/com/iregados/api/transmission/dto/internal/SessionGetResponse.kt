package com.iregados.api.transmission.dto.internal

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class SessionGetResponse(
    val arguments: Arguments,
    val result: String,
) {
    @Serializable
    data class Arguments(
        @SerialName("alt-speed-enabled") val altSpeedEnabled: Boolean,
        @SerialName("session-id") val sessionId: String
    )
}