package com.example.waymate_mobile.utils

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitFactory {
    private const val BASE_URL: String = "http://10.0.2.2:5075"
    // Generic function to create a Retrofit service instance
    fun<T> create(token: String?, repositoryClass: Class<T>): T {
        // Configure Gson to handle date formatting
        val gson = GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            .create()

        val retrofit: Retrofit
        // If a token is provided, create an OkHttpClient with the token included in the headers
        if (token != null) {
            val okHttpClient = OkHttpClientFactory.create(token)
            retrofit = Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        } else {
            // If no token is provided, create a standard Retrofit instance without custom client
            retrofit = Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }
        // Return an implementation of the API endpoints defined in the provided interface class
        return retrofit.create(repositoryClass)
    }
}