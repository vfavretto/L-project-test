package com.itinerary.feature.destinations

sealed interface DestinationsIntent {
    data class LoadDestinations(val tripId: Long) : DestinationsIntent
    data class SearchDestinations(val query: String) : DestinationsIntent
    data class NavigateToDetails(val markId: Long) : DestinationsIntent
}
