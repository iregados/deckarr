package com.iregados.deckarr.api.common.interfaces


interface TorrentInitialData {
    val torrentList: List<Torrent>?
    val torrentSessionStats: TorrentServerStats?
}