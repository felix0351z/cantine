package de.juliando.app

import de.juliando.app.data.ServerDataSource
import de.juliando.app.repository.*

import org.koin.dsl.module

/**
 * Koin module, which includes all necessary data dependencies
 */
val dataModule = module {

    single { ServerDataSource() }
    single<AuthenticationRepository> { AuthenticationRepositoryImpl() }
    single<ContentRepository> { ContentRepositoryImpl() }
    single<PaymentRepository> { PaymentRepositoryImpl() }

}