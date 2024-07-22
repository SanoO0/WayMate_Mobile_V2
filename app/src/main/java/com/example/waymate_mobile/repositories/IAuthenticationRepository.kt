package com.example.waymate_mobile.repositories

import android.util.Log
import com.example.waymate_mobile.dtos.authentication.DtoOutputSigIn
import com.example.waymate_mobile.dtos.authentication.TestLoginResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface IAuthenticationRepository {
    @POST("/api/v1/authentication/login")
    suspend fun signIn(@Body dtoOutputSigIn: DtoOutputSigIn): Response<Void>

    @GET("/api/v1/authentication/TestConnectionDriver")
    suspend fun testTypeUserDriver(): Response<TestLoginResponse>

    @GET("/api/v1/authentication/TestConnectionPassenger")
    suspend fun testTypeUserPassenger(): Response<TestLoginResponse>

    @GET("/api/v1/authentication/TestConnectionAdmin")
    suspend fun testTypeUserAdmin(): Response<TestLoginResponse>

}

