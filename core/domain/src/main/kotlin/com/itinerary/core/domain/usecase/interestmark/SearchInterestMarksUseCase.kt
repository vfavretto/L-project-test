package com.itinerary.core.domain.usecase.interestmark

import com.itinerary.core.domain.model.InterestMark
import com.itinerary.core.domain.repository.InterestMarkRepository
import kotlinx.coroutines.flow.Flow

class SearchInterestMarksUseCase(
    private val interestMarkRepository: InterestMarkRepository
) {
    operator fun invoke(tripId: Long, query: String): Flow<List<InterestMark>> {
        return interestMarkRepository.searchInterestMarks(tripId, query)
    }
}
