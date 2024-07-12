package com.example.waymate_mobile.dtos.users.admin

import java.util.Date

data class DtoInputAdmin(
    val id: Int,
    val userType: String,
    val username: String,
    val password: String,
    val email: String,
    val birthdate: Date,
    val isBanned: Boolean,
    val phoneNumber: String
)