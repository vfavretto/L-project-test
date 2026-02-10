package com.itinerary.feature.details.di

import com.itinerary.feature.details.DetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val detailsModule = module {
    viewModel {
        DetailsViewModel(
            getInterestMarkByIdUseCase = get(),
            updateInterestMarkUseCase = get()
        )
    }
}
