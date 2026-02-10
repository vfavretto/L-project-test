package com.itinerary.core.database.dao

import androidx.room.*
import com.itinerary.core.database.entity.EdgeEntity
import com.itinerary.core.database.entity.InterestMarkEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for TAO edge graph operations
 * Provides efficient relationship management and graph traversal
 */
@Dao
interface EdgeDao {
    // Get all edges from source
    @Query("SELECT * FROM edges WHERE id1 = :id1 AND type = :type ORDER BY timestamp DESC")
    fun getEdgesFromSource(id1: Long, type: String): Flow<List<EdgeEntity>>

    // Get all edges to target
    @Query("SELECT * FROM edges WHERE id2 = :id2 AND type = :type ORDER BY timestamp DESC")
    fun getEdgesToTarget(id2: Long, type: String): Flow<List<EdgeEntity>>

    // Get specific edge
    @Query("SELECT * FROM edges WHERE id1 = :id1 AND id2 = :id2 AND type = :type")
    suspend fun getEdge(id1: Long, id2: Long, type: String): EdgeEntity?

    // Insert edge
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEdge(edge: EdgeEntity)

    // Insert multiple edges
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEdges(edges: List<EdgeEntity>)

    // Delete specific edge
    @Query("DELETE FROM edges WHERE id1 = :id1 AND id2 = :id2 AND type = :type")
    suspend fun deleteEdge(id1: Long, id2: Long, type: String)

    // Delete all edges from source
    @Query("DELETE FROM edges WHERE id1 = :id1 AND type = :type")
    suspend fun deleteEdgesFromSource(id1: Long, type: String)

    // Delete all edges to target
    @Query("DELETE FROM edges WHERE id2 = :id2 AND type = :type")
    suspend fun deleteEdgesToTarget(id2: Long, type: String)

    // Get interest marks by trip using edge graph
    @Query("""
        SELECT im.* FROM interest_marks im
        INNER JOIN edges e ON im.id = e.id2
        WHERE e.id1 = :tripId AND e.type = :type
        ORDER BY e.timestamp DESC
    """)
    fun getInterestMarksByTripIdViaEdges(tripId: Long, type: String): Flow<List<InterestMarkEntity>>

    // Count edges from source
    @Query("SELECT COUNT(*) FROM edges WHERE id1 = :id1 AND type = :type")
    suspend fun countEdgesFromSource(id1: Long, type: String): Int
}
