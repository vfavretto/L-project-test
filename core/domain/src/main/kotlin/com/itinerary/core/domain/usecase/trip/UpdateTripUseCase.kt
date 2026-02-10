package com.itinerary.core.domain.usecase.trip

import com.itinerary.core.domain.model.Trip
import com.itinerary.core.domain.repository.TripRepository

class UpdateTripUseCase(
    private val tripRepository: TripRepository
) {
    suspend operator fun invoke(trip: Trip) {
        tripRepository.updateTrip(trip)
    }
}
