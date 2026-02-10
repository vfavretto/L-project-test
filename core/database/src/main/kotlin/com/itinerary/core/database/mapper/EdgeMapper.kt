package com.itinerary.core.database.mapper

import com.itinerary.core.database.entity.EdgeEntity
import com.itinerary.core.domain.model.Edge
import com.itinerary.core.domain.model.EdgeType

fun EdgeEntity.toDomain(): Edge {
    return Edge(
        id1 = id1,
        id2 = id2,
        type = EdgeType.fromValue(type),
        timestamp = timestamp,
        data = data
    )
}

fun Edge.toEntity(): EdgeEntity {
    return EdgeEntity(
        id1 = id1,
        id2 = id2,
        type = type.value,
        timestamp = timestamp,
        data = data
    )
}
