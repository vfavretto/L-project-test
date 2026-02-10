package com.itinerary.core.domain.usecase.trip

import com.itinerary.core.domain.model.Trip
import com.itinerary.core.domain.repository.TripRepository
import kotlinx.coroutines.flow.Flow

class GetTripByIdUseCase(
    private val tripRepository: TripRepository
) {
    operator fun invoke(id: Long): Flow<Trip?> {
        return tripRepository.getTripById(id)
    }
}
