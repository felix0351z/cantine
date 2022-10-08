package de.felix0351

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import de.felix0351.plugins.*
import de.felix0351.utils.FileHandler


//TODO Beispiel config.yaml kommentieren

//TODO DatatenbankService erstellen
//TODO DAO Interface
//TODO DAO Implementation
//TODO DAO Testing

//TODO Authentifizierung (LDAP vs Session?)
//TODO Routen definieren


fun main() {
    //Load all configurations for the server
    FileHandler.load()

    runServer(FileHandler.configuration.port)
}

fun runServer(port: Int)  = embeddedServer(Netty, port = port, host = "0.0.0.0") {
    configureSecurity()
    configureSerialization()
    configureRouting()
}.start(wait = true)


