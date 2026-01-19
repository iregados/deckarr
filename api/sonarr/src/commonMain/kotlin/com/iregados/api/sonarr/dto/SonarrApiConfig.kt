package com.iregados.api.sonarr.dto

import io.ktor.http.URLProtocol

data class SonarrApiConfig(
    val protocol: URLProtocol,
    val host: String,
    val apiKey: String
)