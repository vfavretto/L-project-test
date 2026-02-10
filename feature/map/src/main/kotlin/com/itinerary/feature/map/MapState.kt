package com.itinerary.feature.map

import com.google.android.gms.maps.model.LatLng
import com.itinerary.core.domain.model.InterestMark

data class MapState(
    val tripId: Long = 0,
    val markers: List<InterestMark> = emptyList(),
    val selectedMarker: InterestMark? = null,
    val isDistanceMode: Boolean = false,
    val cameraPosition: LatLng? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val showAddMarkerDialog: Boolean = false,
    val pendingMarkerPosition: LatLng? = null
)
