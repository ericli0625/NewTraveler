package com.example.eric.newtraveler.koin

import com.example.eric.newtraveler.ui.MainRepository
import com.example.eric.newtraveler.ui.attraction.AttractionListRepository
import com.example.eric.newtraveler.ui.attraction.AttractionListViewModel
import com.example.eric.newtraveler.ui.attraction.detail.AttractionDetailRepository
import com.example.eric.newtraveler.ui.attraction.detail.AttractionDetailViewModel
import com.example.eric.newtraveler.ui.home.HomeRepository
import com.example.eric.newtraveler.ui.home.HomeViewModel
import com.example.eric.newtraveler.ui.weather.WeatherRepository
import com.example.eric.newtraveler.ui.weather.WeatherViewModel
import com.example.eric.newtraveler.ui.weather.detail.WeatherDetailRepository
import com.example.eric.newtraveler.ui.weather.detail.WeatherDetailViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val repositoryModule: Module = module {
    // activity
    factory { MainRepository() }
    // fragment
    factory { HomeRepository() }
    factory { WeatherRepository() }
    factory { WeatherDetailRepository() }
    factory { AttractionListRepository() }
    factory { AttractionDetailRepository() }
}

val viewModule: Module = module {
    // fragment
    viewModel { HomeViewModel(get()) }
    viewModel { WeatherViewModel(get()) }
    viewModel { WeatherDetailViewModel(get()) }
    viewModel { AttractionListViewModel(get()) }
    viewModel { AttractionDetailViewModel(get()) }
}

val appModule = listOf(repositoryModule, viewModule)