package de.juliando.app.android.utils

import de.juliando.app.android.ui.home.HomeViewModel
import de.juliando.app.android.ui.home.views.ReportViewModel
import de.juliando.app.android.ui.landing.LoginViewModel
import de.juliando.app.android.ui.orders.OrdersViewModel
import de.juliando.app.android.ui.orders.views.OrderViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val androidModule = module {
    viewModel { HomeViewModel(get(), get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { ReportViewModel(get(), get()) }
    viewModel { OrdersViewModel(get()) }
    viewModel { OrderViewModel(get()) }
}