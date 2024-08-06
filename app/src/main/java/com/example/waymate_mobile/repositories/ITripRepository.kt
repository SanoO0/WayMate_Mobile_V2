package com.example.waymate_mobile.repositories

import com.example.waymate_mobile.dtos.trip.DtoInputTrip
import retrofit2.http.GET
import retrofit2.http.Query

interface ITripRepository {
    @GET("/api/v1/trip/tripByFilter")
    suspend fun getAllTrip(@Query("userCount") count: Int): List<DtoInputTrip>
}