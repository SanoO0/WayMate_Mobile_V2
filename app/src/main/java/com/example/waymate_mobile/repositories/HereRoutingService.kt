package com.example.waymate_mobile.repositories

import com.example.waymate_mobile.dtos.map.HereRoutingResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface HereRoutingService {
    @GET("v8/routes")
    suspend fun getRoute(
        @Query("apiKey") apiKey: String,
        @Query("transportMode") transportMode: String = "car",
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("return") returnOption: String = "summary"
    ): Response<HereRoutingResponse>
}