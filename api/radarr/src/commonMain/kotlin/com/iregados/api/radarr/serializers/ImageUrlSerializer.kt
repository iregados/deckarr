package com.iregados.api.radarr.serializers

import com.iregados.api.radarr.dto.RadarrImage
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

class ImageUrlSerializer(
    private val base: String
) : KSerializer<RadarrImage> {

    private val delegate = RadarrImage.serializer()
    override val descriptor: SerialDescriptor = delegate.descriptor

    override fun deserialize(decoder: Decoder): RadarrImage {
        val image = delegate.deserialize(decoder)
        return image.copy(
            url = image.url?.toAbsolute(),
        )
    }

    override fun serialize(encoder: Encoder, value: RadarrImage) =
        delegate.serialize(encoder, value)

    private fun String.toAbsolute(): String = "$base$this"
}