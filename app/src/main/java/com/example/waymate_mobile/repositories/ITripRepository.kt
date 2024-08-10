package com.example.waymate_mobile.repositories

import com.example.waymate_mobile.dtos.trip.DtoInputTrip
import com.example.waymate_mobile.dtos.trip.DtoOutputTrip
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ITripRepository {
    @GET("/api/v1/trip/tripByFilter")
    suspend fun getAllTrip(@Query("userCount") count: Int): List<DtoInputTrip>

    @GET("/api/v1/trip/tripByFilterPassenger")
    suspend fun getAllTripPassenger(@Query("userCount") count: Int): List<DtoInputTrip>

    @POST("/api/v1/trip/create")
    suspend fun createTrip(@Body dto: DtoOutputTrip)
}