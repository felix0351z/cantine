package de.felix0351.utils

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

/**
 * Serializer for the LocalDateTime object in Java
 * If the time is null and not provided, it will take the current time
 *
 * @property FORMAT Format as String ("yyyy-MM-dd:HH:mm:ss")
 *
 */
object LocalDateTimeSerializer : KSerializer<LocalDateTime> {

    private const val FORMAT = "yyyy-MM-dd:HH:mm:ss"
    private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(FORMAT)


    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        serialName = "time",
        kind = PrimitiveKind.STRING
    )

    @OptIn(ExperimentalSerializationApi::class)
    override fun deserialize(decoder: Decoder): LocalDateTime {
        val str = decoder.decodeNullableSerializableValue(String.serializer().nullable)

        return if (str == null)  {
            LocalDateTime.now()
        }
        else  {
            LocalDateTime.parse(str, formatter)
        }
    }

    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        val str = value.format(formatter)
        encoder.encodeString(str)
    }

}


object UUIDSerializer : KSerializer<UUID> {

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        serialName = "UUID",
        kind = PrimitiveKind.STRING
    )

    override fun deserialize(decoder: Decoder): UUID {
        return UUID.fromString(decoder.toString())
    }

    override fun serialize(encoder: Encoder, value: UUID) {
        encoder.encodeString(value.toString())
    }

}