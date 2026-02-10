package com.itinerary.core.domain.usecase.trip

import com.itinerary.core.domain.repository.TripRepository

class ToggleFavoriteUseCase(
    private val tripRepository: TripRepository
) {
    suspend operator fun invoke(id: Long) {
        tripRepository.toggleFavorite(id)
    }
}
