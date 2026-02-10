package com.itinerary.feature.destinations

import com.itinerary.core.domain.model.InterestMark

data class DestinationsState(
    val tripId: Long = 0,
    val destinations: List<InterestMark> = emptyList(),
    val filteredDestinations: List<InterestMark> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)
