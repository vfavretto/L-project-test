package com.itinerary.feature.trip.di

import com.itinerary.feature.trip.TripViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val tripModule = module {
    viewModel {
        TripViewModel(
            getTripByIdUseCase = get()
        )
    }
}
