package com.example.waymate_mobile.repositories.users.user

import com.example.waymate_mobile.dtos.users.user.DtoInputUser
import retrofit2.http.GET
import retrofit2.http.Path

interface IUserRepository {
    @GET("api/v1/user/getConnectedUserData")
    suspend fun getUserData(): DtoInputUser

    @GET("api/v1/user/partialUser/{id}")
    suspend fun getUserDataById(@Path("id") id: Int): DtoInputUser
}