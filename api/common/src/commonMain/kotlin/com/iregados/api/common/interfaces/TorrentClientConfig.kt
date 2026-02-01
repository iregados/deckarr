package com.iregados.api.common.interfaces

import io.ktor.http.URLProtocol

interface TorrentClientConfig {
    val protocol: URLProtocol
    val host: String
    val username: String
    val password: String
}