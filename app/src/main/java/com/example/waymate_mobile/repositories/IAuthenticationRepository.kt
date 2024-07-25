package com.example.waymate_mobile.repositories

import com.example.waymate_mobile.dtos.authentication.DtoOutputSigIn
import com.example.waymate_mobile.dtos.authentication.DtoInputAuthentication
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface IAuthenticationRepository {
    @POST("/api/v1/authentication/signIn")
    suspend fun signIn(@Body dtoOutputSigIn: DtoOutputSigIn): Response<Void>

    @GET("/api/v1/authentication")
    fun getUserType(): Call<DtoInputAuthentication>
}

