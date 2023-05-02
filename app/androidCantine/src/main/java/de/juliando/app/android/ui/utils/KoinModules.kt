package de.juliando.app.android.ui.utils

import de.juliando.app.android.ui.home.HomeViewModel
import de.juliando.app.android.ui.landing.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val androidModule = module {
    viewModel { HomeViewModel(get(), get()) }
    viewModel { LoginViewModel(get()) }
}