package com.iregados.deckarr.core.domain

import com.iregados.deckarr.api.common.interfaces.TorrentClientApi
import com.iregados.deckarr.core.domain.dto.DownloadsSettings

interface TorrentClientFactory {
    fun create(settings: DownloadsSettings): TorrentClientApi
}