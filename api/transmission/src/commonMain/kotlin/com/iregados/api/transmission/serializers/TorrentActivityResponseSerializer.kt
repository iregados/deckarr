package com.iregados.api.transmission.serializers

import com.iregados.api.transmission.dto.Torrent
import com.iregados.api.transmission.dto.TorrentActivityResponse
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.float
import kotlinx.serialization.json.int
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.long
import kotlinx.serialization.json.longOrNull

internal object TorrentActivityResponseSerializer : KSerializer<TorrentActivityResponse> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("TorrentActivityResponse") {
            element("torrents", ListSerializer(Torrent.serializer()).descriptor)
            element("removed", ListSerializer(Int.serializer()).descriptor)
        }

    override fun deserialize(decoder: Decoder): TorrentActivityResponse {
        val jsonDecoder = decoder as? JsonDecoder
            ?: throw IllegalStateException("This serializer can only be used with Json format")
        val root = jsonDecoder.decodeJsonElement().jsonObject

        val torrentsArray = root["arguments"]
            ?.jsonObject?.get("torrents")
            ?.jsonArray ?: emptyList()

        val torrents = if (torrentsArray.size > 2) {
            val fieldNames = torrentsArray[0].jsonArray.map { it.jsonPrimitive.content }

            torrentsArray.drop(1).map { torrentEntry ->
                val values = torrentEntry.jsonArray
                val torrentMap = fieldNames.zip(values) { name, value -> name to value }.toMap()
                Torrent(
                    id = torrentMap["id"]?.jsonPrimitive!!.int,
                    name = torrentMap["name"]?.jsonPrimitive!!.contentOrNull,
                    addedDate = torrentMap["addedDate"]?.jsonPrimitive!!.longOrNull,
                    status = torrentMap["status"]?.jsonPrimitive!!.int,
                    totalSize = torrentMap["totalSize"]?.jsonPrimitive!!.long,
                    percentDone = torrentMap["percentDone"]?.jsonPrimitive!!.float,
                    error = torrentMap["error"]?.jsonPrimitive?.intOrNull,
                    errorString = torrentMap["errorString"]?.jsonPrimitive?.contentOrNull,
                    isFinished = torrentMap["isFinished"]?.jsonPrimitive?.booleanOrNull,
                    isStalled = torrentMap["isStalled"]?.jsonPrimitive?.booleanOrNull,
                    rateDownload = torrentMap["rateDownload"]?.jsonPrimitive?.intOrNull,
                    rateUpload = torrentMap["rateUpload"]?.jsonPrimitive?.intOrNull,
                    uploadedEver = torrentMap["uploadedEver"]?.jsonPrimitive?.longOrNull,
                    downloadedEver = torrentMap["downloadedEver"]?.jsonPrimitive?.longOrNull,
                    eta = torrentMap["eta"]?.jsonPrimitive?.intOrNull,
                    queuePosition = torrentMap["queuePosition"]?.jsonPrimitive?.intOrNull,
                    seedRatioMode = torrentMap["seedRatioMode"]?.jsonPrimitive?.intOrNull,
                    seedRatioLimit = torrentMap["seedRatioLimit"]?.jsonPrimitive?.doubleOrNull,
                    peersConnected = torrentMap["peersConnected"]?.jsonPrimitive?.intOrNull,
                    peersGettingFromUs = torrentMap["peersGettingFromUs"]?.jsonPrimitive?.intOrNull,
                    peersSendingToUs = torrentMap["peersSendingToUs"]?.jsonPrimitive?.intOrNull,
                    activityDate = torrentMap["activityDate"]?.jsonPrimitive?.longOrNull,
                )
            }
        } else {
            emptyList()
        }

        val removedArray = root["arguments"]
            ?.jsonObject?.get("removed")
            ?.jsonArray ?: emptyList()

        val removedIds = if (removedArray.isNotEmpty()) {
            removedArray.map {
                it.jsonPrimitive.int
            }
        } else {
            emptyList()
        }

        return TorrentActivityResponse(
            torrents = torrents,
            removed = removedIds
        )
    }

    override fun serialize(encoder: Encoder, value: TorrentActivityResponse) {
        throw UnsupportedOperationException("Serialization not supported")
    }
}