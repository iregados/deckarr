package com.iregados.api.transmission.dto

import io.ktor.http.URLProtocol

data class TransmissionApiConfig(
    val protocol: URLProtocol,
    val host: String,
    val username: String,
    val password: String
)