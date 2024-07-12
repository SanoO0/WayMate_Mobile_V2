package com.example.waymate_mobile.dtos.booking

import java.util.Date

data class DtoInputBooking(
    val id: Int,
    val date: Date,
    val reservedSeats: Int,
    val idPassenger: Int,
    val idTrip: Int
)
