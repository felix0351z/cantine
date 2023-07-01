package de.juliando.app.android.utils

import de.juliando.app.android.ui.home.HomeViewModel
import de.juliando.app.android.ui.home.report.ReportViewModel
import de.juliando.app.android.ui.home.shopping_cart.ShoppingCartViewModel
import de.juliando.app.android.ui.landing.LoginViewModel
import de.juliando.app.android.ui.orders.OrdersViewModel
import de.juliando.app.android.ui.orders.views.OrderViewModel
import de.juliando.app.android.ui.scanner.ScannerViewModel
import de.juliando.app.android.ui.scanner.verify_order.VerifyOrderViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val androidModule = module {
    viewModel { HomeViewModel(get(), get(), get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { ReportViewModel(get(), get()) }
    viewModel { OrdersViewModel(get()) }
    viewModel { OrderViewModel(get(), get()) }
    viewModel { ShoppingCartViewModel(get()) }
    viewModel { ScannerViewModel() }
    viewModel { VerifyOrderViewModel(get(), get()) }
}