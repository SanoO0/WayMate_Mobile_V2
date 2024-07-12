package com.example.waymate_mobile.repositories.users.admin

import com.example.waymate_mobile.dtos.booking.DtoInputBooking
import com.example.waymate_mobile.dtos.users.admin.DtoInputAdmin
import retrofit2.http.GET

interface IAdminRepository {
    @GET("admin")
    suspend fun getAllAdmin(): List<DtoInputAdmin>
}