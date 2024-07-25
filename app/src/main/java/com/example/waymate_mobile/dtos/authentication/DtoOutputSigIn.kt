package com.example.waymate_mobile.dtos.authentication

data class DtoOutputSigIn(
    val email: String,
    val password: String,
    val logged: Boolean
)
