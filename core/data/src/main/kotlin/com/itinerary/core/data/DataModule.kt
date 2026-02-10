package com.itinerary.core.data

import com.itinerary.core.data.repository.InterestMarkRepositoryImpl
import com.itinerary.core.data.repository.TripRepositoryImpl
import com.itinerary.core.domain.repository.InterestMarkRepository
import com.itinerary.core.domain.repository.TripRepository
import org.koin.dsl.module

val dataModule = module {
    single<TripRepository> {
        TripRepositoryImpl(
            tripDao = get(),
            interestMarkDao = get(),
            edgeDao = get()
        )
    }

    single<InterestMarkRepository> {
        InterestMarkRepositoryImpl(
            interestMarkDao = get(),
            edgeDao = get()
        )
    }
}
