package com.iregados.api.common.interfaces

interface TorrentActivityResponse {
    val torrents: List<Torrent>
    val removed: List<String>
}