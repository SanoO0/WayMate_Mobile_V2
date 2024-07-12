package com.example.waymate_mobile.repositories.users.passenger

import com.example.waymate_mobile.dtos.users.passenger.DtoInputPassenger
import retrofit2.http.GET

interface IPassengerRepository {
    @GET("passenger")
    suspend fun getAllPassenger(): List<DtoInputPassenger>
}