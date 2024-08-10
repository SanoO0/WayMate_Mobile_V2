package com.example.waymate_mobile.dtos.trip

import java.util.Date

data class DtoInputTrip(
    val id: Int,
    val idDriver: Int,
    val smoke: Boolean,
    val price: Float,
    val luggage: Boolean,
    val petFriendly: Boolean,
    val date: Date,
    val driverMessage: String,
    val airConditioning: Boolean,
    val cityStartingPoint: String,
    val cityDestination: String,
    val plateNumber: String,
    val brand: String,
    val model: String
)