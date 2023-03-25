package de.juliando.app.android.ui.utils

import de.juliando.app.android.ui.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.annotation.KoinReflectAPI
import org.koin.dsl.module

val androidModule = module {
    viewModel { HomeViewModel(get(), get()) }
}