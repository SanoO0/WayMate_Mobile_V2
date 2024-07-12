package com.example.waymate_mobile.repositories.users.user

import com.example.waymate_mobile.dtos.users.user.DtoInputUser
import retrofit2.http.GET

interface IUserRepository {
    @GET("user")
    suspend fun getAllUser(): List<DtoInputUser>
}