package de.felix0351.utils

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun Any.getLogger(): Logger = LoggerFactory.getLogger(javaClass)

val LoggingPlugin = createApplicationPlugin("CantineLoggingPlugin") {
    onCall { call ->

        // Get the address from the client. If a proxy is used, take the X-Forwarded-Header
        val xForwardedHeader = call.request.headers[HttpHeaders.XForwardedFor]
        val address = xForwardedHeader ?: call.request.local.remoteAddress

        call.application.log.info("Call to ${call.request.uri} from the client $address")
    }


}