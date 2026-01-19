package com.iregados.api.transmission.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TrackerStats(
    val announce: String? = null,
    val id: Int? = null,
    val scrape: String? = null,
    val tier: Int? = null,
    val host: String? = null,
    @SerialName("last-announce-time") val lastAnnounceTime: Long? = null,
    @SerialName("last-scrape-time") val lastScrapeTime: Long? = null,
    @SerialName("has-announced") val hasAnnounced: Boolean? = null,
    @SerialName("has-scraped") val hasScraped: Boolean? = null,
    @SerialName("last-announce-result") val lastAnnounceResult: String? = null,
    @SerialName("last-scrape-result") val lastScrapeResult: String? = null,
    @SerialName("seeder-count") val seederCount: Int? = null,
    @SerialName("leecher-count") val leecherCount: Int? = null,
    @SerialName("download-count") val downloadCount: Int? = null
)