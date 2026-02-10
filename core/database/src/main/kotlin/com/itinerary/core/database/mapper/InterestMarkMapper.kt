package com.itinerary.core.database.mapper

import com.itinerary.core.database.entity.InterestMarkEntity
import com.itinerary.core.domain.model.InterestMark

fun InterestMarkEntity.toDomain(): InterestMark {
    return InterestMark(
        id = id,
        tripId = tripId,
        name = name,
        imageUrl = imageUrl,
        latitude = latitude,
        longitude = longitude,
        scheduledDate = scheduledDate,
        ranking = ranking,
        userNote = userNote,
        tags = if (tags.isBlank()) emptyList() else tags.split(",").map { it.trim() },
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun InterestMark.toEntity(): InterestMarkEntity {
    return InterestMarkEntity(
        id = id,
        tripId = tripId,
        name = name,
        imageUrl = imageUrl,
        latitude = latitude,
        longitude = longitude,
        scheduledDate = scheduledDate,
        ranking = ranking,
        userNote = userNote,
        tags = tags.joinToString(","),
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
