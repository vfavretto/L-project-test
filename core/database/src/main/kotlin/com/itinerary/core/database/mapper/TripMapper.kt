package com.itinerary.core.database.mapper

import com.itinerary.core.database.entity.TripEntity
import com.itinerary.core.domain.model.Trip

fun TripEntity.toDomain(): Trip {
    return Trip(
        id = id,
        name = name,
        imageUrl = imageUrl,
        isFavorite = isFavorite,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun Trip.toEntity(): TripEntity {
    return TripEntity(
        id = id,
        name = name,
        imageUrl = imageUrl,
        isFavorite = isFavorite,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
