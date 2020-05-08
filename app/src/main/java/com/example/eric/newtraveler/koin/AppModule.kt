package com.example.eric.newtraveler.koin

import com.example.eric.newtraveler.ui.MainRepository
import com.example.eric.newtraveler.ui.home.HomeRepository
import com.example.eric.newtraveler.ui.home.HomeViewModel
import com.example.eric.newtraveler.ui.weather.WeatherRepository
import com.example.eric.newtraveler.ui.weather.WeatherViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val repositoryModule: Module = module {
    // activity
    factory { MainRepository() }
    // fragment
    factory { HomeRepository() }
    factory { WeatherRepository() }
}

val viewModule: Module = module {
    // fragment
    viewModel { HomeViewModel(get()) }
    viewModel { WeatherViewModel(get()) }
}

val appModule = listOf(repositoryModule, viewModule)