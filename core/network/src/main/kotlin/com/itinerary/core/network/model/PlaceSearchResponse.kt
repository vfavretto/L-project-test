package com.itinerary.core.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PlaceSearchResponse(
    @Json(name = "results")
    val results: List<PlaceResult>,
    @Json(name = "status")
    val status: String
)

@JsonClass(generateAdapter = true)
data class PlaceResult(
    @Json(name = "place_id")
    val placeId: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "geometry")
    val geometry: Geometry,
    @Json(name = "photos")
    val photos: List<Photo>? = null,
    @Json(name = "rating")
    val rating: Double? = null
)

@JsonClass(generateAdapter = true)
data class Geometry(
    @Json(name = "location")
    val location: Location
)

@JsonClass(generateAdapter = true)
data class Location(
    @Json(name = "lat")
    val lat: Double,
    @Json(name = "lng")
    val lng: Double
)

@JsonClass(generateAdapter = true)
data class Photo(
    @Json(name = "photo_reference")
    val photoReference: String,
    @Json(name = "height")
    val height: Int,
    @Json(name = "width")
    val width: Int
)
