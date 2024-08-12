package com.example.waymate_mobile.dtos.user

import java.util.Date

data class DtoInputUser(
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
