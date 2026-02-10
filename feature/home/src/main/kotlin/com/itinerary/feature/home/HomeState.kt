package com.itinerary.feature.home

import com.itinerary.core.domain.model.Trip

data class HomeState(
    val trips: List<Trip> = emptyList(),
    val filteredTrips: List<Trip> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val showFavoritesOnly: Boolean = false
)
