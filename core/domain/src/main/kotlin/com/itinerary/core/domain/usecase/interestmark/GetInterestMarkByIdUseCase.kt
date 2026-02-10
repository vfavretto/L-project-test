package com.itinerary.core.domain.usecase.interestmark

import com.itinerary.core.domain.model.InterestMark
import com.itinerary.core.domain.repository.InterestMarkRepository
import kotlinx.coroutines.flow.Flow

class GetInterestMarkByIdUseCase(
    private val interestMarkRepository: InterestMarkRepository
) {
    operator fun invoke(id: Long): Flow<InterestMark?> {
        return interestMarkRepository.getInterestMarkById(id)
    }
}
