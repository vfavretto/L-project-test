package com.itinerary.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "interest_marks")
data class InterestMarkEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val tripId: Long,
    val name: String,
    val imageUrl: String?,
    val latitude: Double,
    val longitude: Double,
    val scheduledDate: Long?,
    val ranking: Int,
    val userNote: String?,
    val tags: String, // Stored as comma-separated values
    val createdAt: Long,
    val updatedAt: Long
)
