package com.itinerary.core.domain.usecase.interestmark

import com.itinerary.core.domain.model.InterestMark
import com.itinerary.core.domain.repository.InterestMarkRepository

class CreateInterestMarkUseCase(
    private val interestMarkRepository: InterestMarkRepository
) {
    suspend operator fun invoke(
        tripId: Long,
        name: String,
        latitude: Double,
        longitude: Double,
        imageUrl: String? = null,
        scheduledDate: Long? = null,
        ranking: Int = 3,
        userNote: String? = null,
        tags: List<String> = emptyList()
    ): Long {
        val interestMark = InterestMark(
            tripId = tripId,
            name = name,
            latitude = latitude,
            longitude = longitude,
            imageUrl = imageUrl,
            scheduledDate = scheduledDate,
            ranking = ranking,
            userNote = userNote,
            tags = tags
        )
        return interestMarkRepository.createInterestMark(interestMark)
    }
}
