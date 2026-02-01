package com.iregados.api.transmission.serializers

import com.iregados.api.transmission.dto.TransmissionActivityResponse
import com.iregados.api.transmission.dto.TransmissionTorrent
import com.iregados.api.transmission.dto.toTorrent
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
import kotlinx.serialization.json.float
import kotlinx.serialization.json.int
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.long
import kotlinx.serialization.json.longOrNull

internal object TransmissionTorrentActivityResponseSerializer :
    KSerializer<TransmissionActivityResponse> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("TorrentActivityResponse") {
            element("torrents", ListSerializer(TransmissionTorrent.serializer()).descriptor)
            element("removed", ListSerializer(Int.serializer()).descriptor)
        }

    override fun deserialize(decoder: Decoder): TransmissionActivityResponse {
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
                TransmissionTorrent(
                    id = torrentMap["id"]?.jsonPrimitive!!.content,
                    name = torrentMap["name"]?.jsonPrimitive!!.contentOrNull,
                    addedDate = torrentMap["addedDate"]?.jsonPrimitive!!.longOrNull,
                    status = torrentMap["status"]?.jsonPrimitive!!.int,
                    totalSize = torrentMap["totalSize"]?.jsonPrimitive!!.long,
                    percentDone = torrentMap["percentDone"]?.jsonPrimitive!!.float,
                    isFinished = torrentMap["isFinished"]?.jsonPrimitive?.booleanOrNull,
                    isStalled = torrentMap["isStalled"]?.jsonPrimitive?.booleanOrNull,
                    rateDownload = torrentMap["rateDownload"]?.jsonPrimitive?.intOrNull,
                    rateUpload = torrentMap["rateUpload"]?.jsonPrimitive?.intOrNull,
                    uploadedEver = torrentMap["uploadedEver"]?.jsonPrimitive?.longOrNull,
                    downloadedEver = torrentMap["downloadedEver"]?.jsonPrimitive?.longOrNull,
                    eta = torrentMap["eta"]?.jsonPrimitive?.intOrNull,
                ).toTorrent()
            }
        } else {
            emptyList()
        }

        val removedArray = root["arguments"]
            ?.jsonObject?.get("removed")
            ?.jsonArray ?: emptyList()

        val removedIds = if (removedArray.isNotEmpty()) {
            removedArray.map {
                it.jsonPrimitive.content
            }
        } else {
            emptyList()
        }

        return TransmissionActivityResponse(
            torrents = torrents,
            removed = removedIds
        )
    }

    override fun serialize(encoder: Encoder, value: TransmissionActivityResponse) {
        throw UnsupportedOperationException("Serialization not supported")
    }
}