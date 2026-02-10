package com.itinerary.core.domain.usecase.trip

import com.itinerary.core.domain.model.Trip
import com.itinerary.core.domain.repository.TripRepository
import kotlinx.coroutines.flow.Flow

class SearchTripsUseCase(
    private val tripRepository: TripRepository
) {
    operator fun invoke(query: String): Flow<List<Trip>> {
        return tripRepository.searchTrips(query)
    }
}
