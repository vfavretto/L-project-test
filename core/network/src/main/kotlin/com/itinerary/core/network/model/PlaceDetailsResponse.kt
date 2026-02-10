package com.itinerary.core.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PlaceDetailsResponse(
    @Json(name = "result")
    val result: PlaceDetails?,
    @Json(name = "status")
    val status: String
)

@JsonClass(generateAdapter = true)
data class PlaceDetails(
    @Json(name = "name")
    val name: String?,
    @Json(name = "formatted_address")
    val formattedAddress: String?,
    @Json(name = "geometry")
    val geometry: Geometry?,
    @Json(name = "photos")
    val photos: List<Photo>? = null,
    @Json(name = "rating")
    val rating: Double? = null
)

fun PlaceDetails.getPhotoUrl(apiKey: String, maxWidth: Int = 400): String? {
    val photoReference = photos?.firstOrNull()?.photoReference ?: return null
    return "https://maps.googleapis.com/maps/api/place/photo?maxwidth=$maxWidth&photo_reference=$photoReference&key=$apiKey"
}
