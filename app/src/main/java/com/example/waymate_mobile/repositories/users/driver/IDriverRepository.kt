package com.example.waymate_mobile.repositories.users.driver

import com.example.waymate_mobile.dtos.users.driver.DtoInputDriver
import retrofit2.http.GET

interface IDriverRepository {
    @GET("driver")
    suspend fun getAllDriver(): List<DtoInputDriver>
}