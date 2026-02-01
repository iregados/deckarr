package com.iregados.api.common.interfaces


interface TorrentInitialData {
    val torrentList: List<Torrent>?
    val torrentSessionStats: TorrentServerStats?
}