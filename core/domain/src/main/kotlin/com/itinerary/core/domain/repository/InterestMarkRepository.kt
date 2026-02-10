package com.itinerary.core.domain.repository

import com.itinerary.core.domain.model.InterestMark
import kotlinx.coroutines.flow.Flow

interface InterestMarkRepository {
    fun getInterestMarksByTripId(tripId: Long): Flow<List<InterestMark>>
    
    fun getInterestMarkById(id: Long): Flow<InterestMark?>
    
    fun getScheduledInterestMarks(tripId: Long): Flow<List<InterestMark>>
    
    fun searchInterestMarks(tripId: Long, query: String): Flow<List<InterestMark>>
    
    suspend fun createInterestMark(interestMark: InterestMark): Long
    
    suspend fun updateInterestMark(interestMark: InterestMark)
    
    suspend fun deleteInterestMark(id: Long)
    
    suspend fun getInterestMarkCount(tripId: Long): Int
}
