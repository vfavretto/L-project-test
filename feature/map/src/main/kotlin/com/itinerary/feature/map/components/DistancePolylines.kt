package com.itinerary.feature.map.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Polyline
import com.itinerary.core.domain.model.InterestMark

@Composable
fun DistancePolylines(markers: List<InterestMark>) {
    if (markers.size < 2) return

    // Sort markers by creation date to draw lines in order
    val sortedMarkers = markers.sortedBy { it.createdAt }

    // Draw polyline connecting all markers
    val points = sortedMarkers.map { marker ->
        LatLng(marker.latitude, marker.longitude)
    }

    Polyline(
        points = points,
        color = Color(0xFF2E7D32), // Green color matching theme
        width = 8f
    )
}
