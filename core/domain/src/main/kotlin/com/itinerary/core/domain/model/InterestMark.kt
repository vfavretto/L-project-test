package com.itinerary.core.domain.model

data class InterestMark(
    val id: Long = 0,
    val tripId: Long,
    val name: String,
    val imageUrl: String? = null,
    val latitude: Double,
    val longitude: Double,
    val scheduledDate: Long? = null,
    val ranking: Int = 3, // 1-5
    val userNote: String? = null,
    val tags: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
