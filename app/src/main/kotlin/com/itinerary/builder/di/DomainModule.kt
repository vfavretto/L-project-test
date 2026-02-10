package com.itinerary.builder.di

import com.itinerary.core.domain.usecase.interestmark.*
import com.itinerary.core.domain.usecase.trip.*
import org.koin.dsl.module

val domainModule = module {
    // Trip use cases
    factory { GetAllTripsUseCase(get()) }
    factory { GetTripByIdUseCase(get()) }
    factory { CreateTripUseCase(get()) }
    factory { UpdateTripUseCase(get()) }
    factory { DeleteTripUseCase(get()) }
    factory { ToggleFavoriteUseCase(get()) }
    factory { SearchTripsUseCase(get()) }
    
    // Interest mark use cases
    factory { GetInterestMarksByTripIdUseCase(get()) }
    factory { GetInterestMarkByIdUseCase(get()) }
    factory { CreateInterestMarkUseCase(get()) }
    factory { UpdateInterestMarkUseCase(get()) }
    factory { DeleteInterestMarkUseCase(get()) }
    factory { GetScheduledInterestMarksUseCase(get()) }
    factory { SearchInterestMarksUseCase(get()) }
}
