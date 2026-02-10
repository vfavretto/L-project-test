package com.itinerary.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trips")
data class TripEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val imageUrl: String?,
    val isFavorite: Boolean,
    val createdAt: Long,
    val updatedAt: Long
)
