package com.itinerary.core.database.entity

import androidx.room.Entity
import androidx.room.Index

/**
 * TAO-inspired edge graph entity for managing relationships between entities
 * Provides efficient graph traversal and relationship management
 */
@Entity(
    tableName = "edges",
    primaryKeys = ["id1", "id2", "type"],
    indices = [
        Index(value = ["id1", "type"]),
        Index(value = ["id2", "type"]),
        Index(value = ["timestamp"])
    ]
)
data class EdgeEntity(
    val id1: Long,      // Source entity ID (e.g., Trip ID)
    val id2: Long,      // Target entity ID (e.g., InterestMark ID)
    val type: String,   // Edge type (e.g., "trip_interest_mark")
    val timestamp: Long,
    val data: String?   // Optional JSON metadata
)
