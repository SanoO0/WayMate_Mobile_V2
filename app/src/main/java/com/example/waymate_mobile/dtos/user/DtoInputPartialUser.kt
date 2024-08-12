package com.example.waymate_mobile.dtos.user

data class DtoInputPartialUser(
    val email: String,
    val phoneNumber: String,
    val lastName: String?,
    val gender: String?
)
