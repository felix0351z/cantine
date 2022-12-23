package de.felix0351.plugins

import de.felix0351.db.MongoDBConnection
import de.felix0351.dependencies.CantineService
import de.felix0351.dependencies.AuthenticationRepositoryMongoDB
import de.felix0351.dependencies.ContentRepositoryMongoDB
import de.felix0351.dependencies.AuthenticationRepository
import de.felix0351.dependencies.ContentRepository
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.dsl.module
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger


fun Application.configureDependencyInjection() {

    install(Koin) {
        slf4jLogger()
        modules(appModule)
    }

}

val appModule = module {

    single { MongoDBConnection() }
    single<AuthenticationRepository> { AuthenticationRepositoryMongoDB(get()) }
    single<ContentRepository> { ContentRepositoryMongoDB(get()) }
    single { CantineService(get(), get()) }

}

/**
 *  Injects a CantineService instance via Koin
 */
fun Route.withInjection(route: Route.(service: CantineService) -> Unit) {
    val service by inject<CantineService>()
    return route(service)
}
