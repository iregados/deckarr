package com.iregados.deckarr.core.data.repository

import com.iregados.deckarr.api.common.interfaces.TorrentClientApi
import com.iregados.deckarr.api.common.util.ConnectionAddress
import com.iregados.deckarr.api.qbittorrent.QBitTorrentApi
import com.iregados.deckarr.api.transmission.TransmissionApi
import com.iregados.deckarr.core.domain.TorrentClientFactory
import com.iregados.deckarr.core.domain.dto.DownloadsSettings
import com.iregados.deckarr.core.domain.util.TorrentClientOption

class TorrentClientFactoryImpl : TorrentClientFactory {
    override fun create(settings: DownloadsSettings): TorrentClientApi {
        val address = ConnectionAddress.parse(settings.connectionAddress)

        return when (settings.selectedClient) {
            TorrentClientOption.Transmission -> TransmissionApi(
                protocol = address.protocol,
                host = address.host,
                username = settings.username,
                password = settings.password,
                basePath = address.path
            )

            TorrentClientOption.QBitTorrent -> QBitTorrentApi(
                protocol = address.protocol,
                host = address.host,
                username = settings.username,
                password = settings.password,
                basePath = address.path
            )
        }
    }
}