package com.itinerary.feature.trip

import com.itinerary.core.domain.model.Trip

data class TripState(
    val trip: Trip? = null,
    val currentDestination: TripDestination = TripDestination.Map,
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class TripDestination(val route: String, val label: String, val icon: String) {
    data object Map : TripDestination("map", "Mapa", "map")
    data object Destinations : TripDestination("destinations", "Destinos", "list")
    data object Schedule : TripDestination("schedule", "Agenda", "calendar")
}
