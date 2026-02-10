package com.itinerary.feature.trip

sealed interface TripIntent {
    data class LoadTrip(val tripId: Long) : TripIntent
    data class NavigateToDestination(val destination: TripDestination) : TripIntent
}
