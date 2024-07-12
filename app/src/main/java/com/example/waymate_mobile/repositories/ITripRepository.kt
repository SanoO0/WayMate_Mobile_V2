package com.example.waymate_mobile.repositories

import com.example.waymate_mobile.dtos.trip.DtoInputTrip
import retrofit2.http.GET

interface ITripRepository {
    @GET("trip")
    suspend fun getAllTrip(): List<DtoInputTrip>
}