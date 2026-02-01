package com.iregados.api.transmission.dto

import kotlinx.serialization.Serializable

@Serializable
data class QBitTorrentTracker(
    val announce: String? = null,
    val id: Int? = null,
    val scrape: String? = null,
    val tier: Int? = null
)