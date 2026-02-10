package com.itinerary.feature.home.di

import com.itinerary.feature.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val homeModule = module {
    viewModel {
        HomeViewModel(
            getAllTripsUseCase = get(),
            searchTripsUseCase = get(),
            toggleFavoriteUseCase = get(),
            deleteTripUseCase = get()
        )
    }
}
