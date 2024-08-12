package com.example.waymate_mobile.repositories

import com.example.waymate_mobile.dtos.map.HereGeocodingResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface HereGeocodingService {
    @GET("v1/geocode")
    suspend fun geocodeAddress(
        @Query("apiKey") apiKey: String,
        @Query("q") address: String
    ): Response<HereGeocodingResponse>
}