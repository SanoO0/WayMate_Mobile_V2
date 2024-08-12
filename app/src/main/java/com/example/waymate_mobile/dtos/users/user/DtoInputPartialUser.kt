package com.example.waymate_mobile.dtos.users.user

import java.util.Date

data class DtoInputPartialUser(
    val email: String,
    val phoneNumber: String,
    val lastName: String?,
    val gender: String?
)
