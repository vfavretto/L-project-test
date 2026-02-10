package com.itinerary.core.domain.usecase.trip

import com.itinerary.core.domain.model.Trip
import com.itinerary.core.domain.repository.TripRepository

class CreateTripUseCase(
    private val tripRepository: TripRepository
) {
    suspend operator fun invoke(name: String, imageUrl: String? = null): Long {
        val trip = Trip(
            name = name,
            imageUrl = imageUrl
        )
        return tripRepository.createTrip(trip)
    }
}
