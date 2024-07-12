package com.example.waymate_mobile.repositories

import com.example.waymate_mobile.dtos.booking.DtoInputBooking
import retrofit2.http.GET

interface IBookingRepository {
    @GET("booking")
    suspend fun getAllBooking(): List<DtoInputBooking>
}