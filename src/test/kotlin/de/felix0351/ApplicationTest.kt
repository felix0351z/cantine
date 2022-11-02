package de.felix0351

import de.felix0351.db.DatabaseService
import de.felix0351.plugins.configureDependencyInjection
import de.felix0351.plugins.configureRouting
import de.felix0351.plugins.configureSecurity
import de.felix0351.plugins.configureSerialization
import de.felix0351.utils.FileHandler
import io.ktor.server.testing.*


fun testModule(func: suspend ApplicationTestBuilder.() -> Unit) = testApplication {
    application {
        FileHandler.load()
        DatabaseService.init()

        configureDependencyInjection()
        configureSecurity()
        configureSerialization()
        configureRouting()
    }

    func()
}




