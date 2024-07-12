package com.example.waymate_mobile.utils

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitFactory {
    private val _url: String = "https://localhost:7210/api/v1/"
    val instance: Retrofit = Retrofit
        .Builder()
        .baseUrl(_url)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}