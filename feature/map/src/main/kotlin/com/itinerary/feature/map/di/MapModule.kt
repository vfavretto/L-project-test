package com.itinerary.feature.map.di

import com.itinerary.feature.map.MapViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mapModule = module {
    viewModel {
        MapViewModel(
            getInterestMarksByTripIdUseCase = get(),
            createInterestMarkUseCase = get(),
            deleteInterestMarkUseCase = get()
        )
    }
}
