package com.itinerary.core.common.utils

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

object DistanceCalculator {
    private const val EARTH_RADIUS_KM = 6371.0

    /**
     * Calculate distance between two coordinates using Haversine formula
     * @return distance in kilometers
     */
    fun calculateDistance(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double
    ): Double {
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)

        val a = sin(dLat / 2).pow(2) +
                cos(Math.toRadians(lat1)) *
                cos(Math.toRadians(lat2)) *
                sin(dLon / 2).pow(2)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return EARTH_RADIUS_KM * c
    }

    /**
     * Format distance with appropriate unit
     */
    fun formatDistance(distanceKm: Double): String {
        return when {
            distanceKm < 1.0 -> "${(distanceKm * 1000).toInt()} m"
            distanceKm < 10.0 -> String.format("%.1f km", distanceKm)
            else -> "${distanceKm.toInt()} km"
        }
    }

    /**
     * Calculate total distance of a route (list of coordinates)
     */
    fun calculateRouteDistance(coordinates: List<Pair<Double, Double>>): Double {
        if (coordinates.size < 2) return 0.0

        var totalDistance = 0.0
        for (i in 0 until coordinates.size - 1) {
            val (lat1, lon1) = coordinates[i]
            val (lat2, lon2) = coordinates[i + 1]
            totalDistance += calculateDistance(lat1, lon1, lat2, lon2)
        }

        return totalDistance
    }
}
