package com.iregados.api.transmission.dto

import com.iregados.api.common.interfaces.TorrentClientConfig
import io.ktor.http.URLProtocol

data class TransmissionApiConfig(
    override val protocol: URLProtocol,
    override val host: String,
    override val username: String,
    override val password: String
) : TorrentClientConfig
