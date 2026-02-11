package com.itinerary.feature.home

sealed interface HomeIntent {
    data object LoadTrips : HomeIntent
    data class SearchTrips(val query: String) : HomeIntent
    data object ToggleFavoritesFilter : HomeIntent
    data class NavigateToTrip(val tripId: Long) : HomeIntent
    data class ToggleFavorite(val tripId: Long) : HomeIntent
    data object ShowAddTripDialog : HomeIntent
    data object DismissAddTripDialog : HomeIntent
    data class CreateTrip(val name: String, val imageUrl: String?) : HomeIntent
    data class DeleteTrip(val tripId: Long) : HomeIntent
}
