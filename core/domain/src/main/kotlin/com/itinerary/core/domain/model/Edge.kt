package com.itinerary.core.domain.model

/**
 * Represents a relationship between two entities in the TAO edge graph
 */
data class Edge(
    val id1: Long,      // Source entity ID (e.g., Trip ID)
    val id2: Long,      // Target entity ID (e.g., InterestMark ID)
    val type: EdgeType,
    val timestamp: Long = System.currentTimeMillis(),
    val data: String? = null  // Optional JSON metadata
)

enum class EdgeType(val value: String) {
    TRIP_INTEREST_MARK("trip_interest_mark"),
    TRIP_FAVORITE("trip_favorite"),
    MARK_SCHEDULED("mark_scheduled");

    companion object {
        fun fromValue(value: String): EdgeType {
            return entries.find { it.value == value }
                ?: throw IllegalArgumentException("Unknown EdgeType: $value")
        }
    }
}
