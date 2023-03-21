package de.juliando.app.utils

import de.juliando.app.data.LocalDataStore
import io.ktor.http.*
import io.ktor.util.date.*
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.*
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Serializer for the Instant object in kotlinx
 * If the time is null and not provided, it will take the current time
 *
 */
object InstantSerializer : KSerializer<Instant> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        serialName = "Instant",
        kind = PrimitiveKind.LONG)

    @OptIn(ExperimentalSerializationApi::class)
    override fun deserialize(decoder: Decoder): Instant {
        val long = decoder.decodeNullableSerializableValue(Long.serializer().nullable)

        return if (long == null) Clock.System.now()
        else Instant.fromEpochMilliseconds(long)
    }

    override fun serialize(encoder: Encoder, value: Instant) =
        encoder.encodeLong(value.toEpochMilliseconds())
}

/**
 * Serializer for the picture attribute in models
 * An correct address to the picture attribute will be added as prefix
 * Pictures can now be directly loaded via the picture attribute.
 *
 */
object PictureSerializer : KSerializer<String?> {

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        serialName = "Picture",
        kind = PrimitiveKind.STRING
    )

    @OptIn(ExperimentalSerializationApi::class)
    override fun deserialize(decoder: Decoder): String? {
        val id = decoder.decodeNullableSerializableValue(String.serializer().nullable) ?: return null
        return "${LocalDataStore.url}/content/image/$id"
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun serialize(encoder: Encoder, value: String?) {
        val id = value?.removePrefix("${LocalDataStore.url}/content/image/")

        if (id == null) encoder.encodeNull()
        else encoder.encodeString(id)
    }

}