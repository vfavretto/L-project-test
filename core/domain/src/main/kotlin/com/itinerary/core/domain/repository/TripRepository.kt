package com.itinerary.core.domain.repository

import com.itinerary.core.domain.model.Trip
import kotlinx.coroutines.flow.Flow

interface TripRepository {
    fun getAllTrips(): Flow<List<Trip>>
    
    fun getTripById(id: Long): Flow<Trip?>
    
    fun getFavoriteTrips(): Flow<List<Trip>>
    
    fun searchTrips(query: String): Flow<List<Trip>>
    
    suspend fun createTrip(trip: Trip): Long
    
    suspend fun updateTrip(trip: Trip)
    
    suspend fun deleteTrip(id: Long)
    
    suspend fun toggleFavorite(id: Long)
}
