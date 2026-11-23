package com.iregados.deckarr.api.qbittorrent.dto

import com.iregados.deckarr.api.common.interfaces.TorrentClientConfig
import io.ktor.http.URLProtocol

data class QBitTorrentApiConfig(
    override val protocol: URLProtocol,
    override val host: String,
    override val username: String,
    override val password: String
) : TorrentClientConfig