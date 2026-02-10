package com.itinerary.core.domain.usecase.interestmark

import com.itinerary.core.domain.repository.InterestMarkRepository

class DeleteInterestMarkUseCase(
    private val interestMarkRepository: InterestMarkRepository
) {
    suspend operator fun invoke(id: Long) {
        interestMarkRepository.deleteInterestMark(id)
    }
}
