package com.itinerary.core.database.dao

import androidx.room.*
import com.itinerary.core.database.entity.TripEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TripDao {
    @Query("SELECT * FROM trips ORDER BY createdAt DESC")
    fun getAllTrips(): Flow<List<TripEntity>>

    @Query("SELECT * FROM trips WHERE id = :id")
    fun getTripById(id: Long): Flow<TripEntity?>

    @Query("SELECT * FROM trips WHERE isFavorite = 1 ORDER BY updatedAt DESC")
    fun getFavoriteTrips(): Flow<List<TripEntity>>

    @Query("SELECT * FROM trips WHERE name LIKE '%' || :query || '%' ORDER BY createdAt DESC")
    fun searchTrips(query: String): Flow<List<TripEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrip(trip: TripEntity): Long

    @Update
    suspend fun updateTrip(trip: TripEntity)

    @Query("DELETE FROM trips WHERE id = :id")
    suspend fun deleteTrip(id: Long)

    @Query("UPDATE trips SET isFavorite = NOT isFavorite, updatedAt = :timestamp WHERE id = :id")
    suspend fun toggleFavorite(id: Long, timestamp: Long)

    @Query("SELECT COUNT(*) FROM trips")
    suspend fun getTripCount(): Int
}
