package de.felix0351

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import de.felix0351.plugins.*
import de.felix0351.utils.FileHandler
import de.felix0351.utils.getLogger

const val VERSION = 1.0

fun main() {
    //Load all configurations for the server
    FileHandler.load()

    runServer(FileHandler.configuration.port)
}

fun runServer(port: Int)  = embeddedServer(Netty, port = port, host = "0.0.0.0") {
    configureDependencyInjection()
    configureSecurity()
    configureSerialization()
    configureRouting()

    getLogger().info("\n" +
            " _________                __  .__                \n" +
            "\\_   ___ \\_____    _____/  |_|__| ____   ____   \n" +
            "/    \\  \\/\\__  \\  /    \\   __\\  |/    \\_/ __ \\  \n" +
            "\\     \\____/ __ \\|   |  \\  | |  |   |  \\  ___/  \n" +
            " \\______  (____  /___|  /__| |__|___|  /\\___  > \n" +
            "        \\/     \\/     \\/             \\/     \\/  " + " v. $VERSION \n" +
            "Authors: Felix Zimmermann & Julian Dold")
}.start(wait = true)


