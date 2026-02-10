package com.itinerary.core.domain.model

data class Trip(
    val id: Long = 0,
    val name: String,
    val imageUrl: String? = null,
    val isFavorite: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
