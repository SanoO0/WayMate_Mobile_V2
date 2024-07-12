package com.example.waymate_mobile.dtos.users.driver

import java.util.Date

data class DtoInputDriver(
    val id: Int,
    val userType: String,
    val username: String,
    val password: String,
    val email: String,
    val birthdate: Date,
    val isBanned: Boolean,
    val phoneNumber: String,
    val lastName: String?,
    val firstName: String?,
    val gender: String?,
    val city: String?
)