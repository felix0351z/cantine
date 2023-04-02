package de.juliando.app

import de.juliando.app.repository.AuthenticationRepository
import de.juliando.app.repository.ContentRepository
import de.juliando.app.repository.PaymentRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin

/**
* Helper class for swift code to access data dependencies
* @property contentRepository Direct access to the content repository
* @property authenticationRepository Direct access to the authentication repository
* @property paymentRepository Direct access to the payment repository
*
**/
class KoinHelper: KoinComponent {

    val contentRepository: ContentRepository by inject()
    val authenticationRepository: AuthenticationRepository by inject()
    val paymentRepository: PaymentRepository by inject()

}

/*
* Start Koin with the data module with all repositories
*/
fun initKoin() {
    startKoin {
        modules(dataModule)
    }
}