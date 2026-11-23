package com.iregados.deckarr.api.transmission.dto.internal

import kotlinx.serialization.Serializable

@Serializable
internal data class SessionGetRequest(
    val method: String = "session-get"
)