package de.felix0351

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import de.felix0351.plugins.*
import de.felix0351.utils.FileHandler
import java.lang.Exception
import kotlin.system.exitProcess




//TODO Debug databaseService

//TODO DAO Interface
//TODO DAO Implementation
//TODO DAO Testing

//TODO Authentifizierung (LDAP vs Session?)
//TODO Routen definieren


//Muss zwischendurch mal gemacht werden
//TODO SQL Tabellen Varchar Größen in Konstanten auslagern
//TODO Beispiel config.yaml kommentieren


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


fun fail(e: Exception): Nothing {
    e.printStackTrace()
    exitProcess(1)
}


