package com.iregados.deckarr.core.domain.dto

import com.iregados.deckarr.core.domain.util.TorrentClientOption

data class DownloadsSettings(
    val selectedClient: TorrentClientOption,
    val connectionAddress: String,
    val username: String,
    val password: String
)
