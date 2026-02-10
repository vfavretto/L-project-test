package com.itinerary.core.domain.usecase.interestmark

import com.itinerary.core.domain.model.InterestMark
import com.itinerary.core.domain.repository.InterestMarkRepository
import kotlinx.coroutines.flow.Flow

class GetScheduledInterestMarksUseCase(
    private val interestMarkRepository: InterestMarkRepository
) {
    operator fun invoke(tripId: Long): Flow<List<InterestMark>> {
        return interestMarkRepository.getScheduledInterestMarks(tripId)
    }
}
