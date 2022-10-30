package de.felix0351.plugins

import de.felix0351.dependencies.CantineService
import de.felix0351.dependencies.AuthenticationRepositoryImpl
import de.felix0351.dependencies.ContentRepositoryImpl
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

    single<AuthenticationRepository> { AuthenticationRepositoryImpl() }
    single<ContentRepository> { ContentRepositoryImpl() }
    single { CantineService(get(), get()) }

}

/**
 *  Injects a CantineService instance via Koin
 */
fun Route.withInjection(route: Route.(service: CantineService) -> Unit) {
    val service by inject<CantineService>()
    return route(service)
}
