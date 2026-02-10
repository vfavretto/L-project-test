package com.itinerary.core.network.api

import com.itinerary.core.network.model.PlaceDetailsResponse
import com.itinerary.core.network.model.PlaceSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GooglePlacesApi {
    @GET("place/nearbysearch/json")
    suspend fun searchNearby(
        @Query("location") location: String,
        @Query("radius") radius: Int = 1000,
        @Query("key") apiKey: String
    ): PlaceSearchResponse

    @GET("place/details/json")
    suspend fun getPlaceDetails(
        @Query("place_id") placeId: String,
        @Query("fields") fields: String = "name,formatted_address,geometry,photos,rating",
        @Query("key") apiKey: String
    ): PlaceDetailsResponse

    @GET("geocode/json")
    suspend fun reverseGeocode(
        @Query("latlng") latLng: String,
        @Query("key") apiKey: String
    ): PlaceDetailsResponse
}
