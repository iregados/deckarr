package com.iregados.deckarr.api.common.util

import io.ktor.http.URLProtocol

data class ConnectionAddress(
    val protocol: URLProtocol,
    val host: String,
    val path: String = ""
) {
    companion object {
        fun parse(raw: String): ConnectionAddress {
            val trimmed = raw.trim()
            val afterProtocol = if (trimmed.contains("://")) {
                trimmed.substringAfter("://")
            } else {
                trimmed
            }

            val host = afterProtocol.substringBefore("/")
            val rawPath = afterProtocol.substringAfter("/", missingDelimiterValue = "")
            val path = if (rawPath.isBlank()) "" else "/" + rawPath.trim('/')

            return ConnectionAddress(
                protocol = if (trimmed.startsWith("https")) URLProtocol.HTTPS else URLProtocol.HTTP,
                host = host,
                path = path
            )
        }
    }
}