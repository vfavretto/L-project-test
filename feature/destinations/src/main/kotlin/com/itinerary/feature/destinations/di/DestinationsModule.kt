package com.itinerary.feature.destinations.di

import com.itinerary.feature.destinations.DestinationsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val destinationsModule = module {
    viewModel {
        DestinationsViewModel(
            getInterestMarksByTripIdUseCase = get()
        )
    }
}
