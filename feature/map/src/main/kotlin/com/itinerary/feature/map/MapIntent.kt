package com.itinerary.feature.map

import com.google.android.gms.maps.model.LatLng

sealed interface MapIntent {
    data class LoadMarkers(val tripId: Long) : MapIntent
    data class AddMarkerAtPosition(val position: LatLng) : MapIntent
    data class SaveMarker(val name: String, val ranking: Int) : MapIntent
    data object CancelAddMarker : MapIntent
    data class SelectMarker(val markerId: Long) : MapIntent
    data object ClearSelection : MapIntent
    data object ToggleDistanceMode : MapIntent
    data object RecenterMap : MapIntent
    data class UpdateMarker(val markerId: Long) : MapIntent
    data class DeleteMarker(val markerId: Long) : MapIntent
}
