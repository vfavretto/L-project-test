package com.itinerary.core.database.dao

import androidx.room.*
import com.itinerary.core.database.entity.InterestMarkEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InterestMarkDao {
    @Query("SELECT * FROM interest_marks WHERE tripId = :tripId ORDER BY createdAt DESC")
    fun getInterestMarksByTripId(tripId: Long): Flow<List<InterestMarkEntity>>

    @Query("SELECT * FROM interest_marks WHERE id = :id")
    fun getInterestMarkById(id: Long): Flow<InterestMarkEntity?>

    @Query("""
        SELECT * FROM interest_marks 
        WHERE tripId = :tripId AND scheduledDate IS NOT NULL 
        ORDER BY scheduledDate ASC
    """)
    fun getScheduledInterestMarks(tripId: Long): Flow<List<InterestMarkEntity>>

    @Query("""
        SELECT * FROM interest_marks 
        WHERE tripId = :tripId AND name LIKE '%' || :query || '%'
        ORDER BY createdAt DESC
    """)
    fun searchInterestMarks(tripId: Long, query: String): Flow<List<InterestMarkEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInterestMark(interestMark: InterestMarkEntity): Long

    @Update
    suspend fun updateInterestMark(interestMark: InterestMarkEntity)

    @Query("DELETE FROM interest_marks WHERE id = :id")
    suspend fun deleteInterestMark(id: Long)

    @Query("DELETE FROM interest_marks WHERE tripId = :tripId")
    suspend fun deleteInterestMarksByTripId(tripId: Long)

    @Query("SELECT COUNT(*) FROM interest_marks WHERE tripId = :tripId")
    suspend fun getInterestMarkCount(tripId: Long): Int
}
