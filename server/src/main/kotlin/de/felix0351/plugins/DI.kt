package de.felix0351.plugins

import de.felix0351.db.MongoDBConnection
import de.felix0351.repository.*
import de.felix0351.services.AuthenticationService
import de.felix0351.services.PaymentService
import io.ktor.server.application.*
import org.koin.dsl.module
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
    single<PaymentRepository> { PaymentRepositoryMongoDB(get()) }
    single { PaymentService(get(), get()) }
    single { AuthenticationService(get()) }

}