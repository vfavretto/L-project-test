package com.itinerary.core.domain.usecase.interestmark

import com.itinerary.core.domain.model.InterestMark
import com.itinerary.core.domain.repository.InterestMarkRepository

class UpdateInterestMarkUseCase(
    private val interestMarkRepository: InterestMarkRepository
) {
    suspend operator fun invoke(interestMark: InterestMark) {
        interestMarkRepository.updateInterestMark(interestMark)
    }
}
