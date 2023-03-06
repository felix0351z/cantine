package de.juliando.app.utils

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
import kotlinx.serialization.json.Json

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
 * Serializer for the Cookie object in ktor
 *
 */
@Serializable
data class CookieSerializable(
    val name: String,
    val value: String,
    val encoding: CookieEncoding = CookieEncoding.URI_ENCODING,
    val maxAge: Int = 0,
    val expires: Long? = null,
    val domain: String? = null,
    val path: String? = null,
    val secure: Boolean = false,
    val httpOnly: Boolean = false,
) {
    companion object{
        fun serializeCookie (cookie: Cookie): String{
            val nowTimestamp = GMTDate().timestamp + (1000 * 20 * 60)
            val temp = CookieSerializable(
                cookie.name,
                cookie.value,
                cookie.encoding,
                cookie.maxAge,
                if (cookie.expires == null) nowTimestamp else cookie.expires?.timestamp,
                cookie.domain,
                cookie.path,
                cookie.secure,
                cookie.httpOnly
            )

            return Json.encodeToString(temp)
        }

        fun deserializeCookie(cookieString: String?): Cookie?{
            if (cookieString == null) return null
            val temp = Json.decodeFromString<CookieSerializable>(cookieString)
            return Cookie(
                temp.name,
                temp.value,
                CookieEncoding.URI_ENCODING,
                temp.maxAge,
                if (temp.expires == null) null else GMTDate(temp.expires),
                temp.domain,
                temp.path,
                temp.secure,
                temp.httpOnly
            )
        }
    }
}