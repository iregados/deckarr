package com.iregados.api.radarr.dto

import io.ktor.http.URLProtocol

data class RadarrApiConfig(
    val protocol: URLProtocol,
    val host: String,
    val apiKey: String
)