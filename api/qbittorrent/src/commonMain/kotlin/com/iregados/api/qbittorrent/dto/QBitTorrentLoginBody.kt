package com.iregados.api.transmission.dto

import kotlinx.serialization.Serializable

@Serializable
data class QBitTorrentLoginBody(
    val username: String,
    val password: String
)