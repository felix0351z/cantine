package de.felix0351.utils

import de.felix0351.models.errors.IllegalIdException
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.litote.kmongo.Id
import org.litote.kmongo.id.IdGenerator
import java.lang.Exception
import java.time.Instant
import java.util.UUID

/**
 * Serializer for the Instant object in Java
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

        return if (long == null) Instant.now()
        else Instant.ofEpochMilli(decoder.decodeLong())
    }

    override fun serialize(encoder: Encoder, value: Instant) =
        encoder.encodeLong(value.toEpochMilli())
}

class CustomIDSerializer<T: Id<*>> : KSerializer<T> {

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        serialName = "IdSerializer",
        kind = PrimitiveKind.STRING)


    @OptIn(ExperimentalSerializationApi::class)
    override fun deserialize(decoder: Decoder): T {
        val str = decoder.decodeNullableSerializableValue(String.serializer().nullable)

        // If a correct id was provided take this (or throw an illegalid exception)
        //If no id is provided create a new
        try {
            return if (str == null) IdGenerator.defaultGenerator.generateNewId<T>() as T
            else IdGenerator.defaultGenerator.create(str) as T

        } catch (ex: Exception) {
            throw IllegalIdException()
        }

    }



    override fun serialize(encoder: Encoder, value: T) {
        encoder.encodeString(value.toString())
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