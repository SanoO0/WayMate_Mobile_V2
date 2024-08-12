package com.example.waymate_mobile.utils

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

object OkHttpClientFactory {
    // Function to create a customized OkHttpClient instance
    fun create(token: String): OkHttpClient {
        // Custom CookieJar to handle cookies manually
        val cookieJar = object : CookieJar {
            // List to store cookies
            private val cookies = mutableListOf<Cookie>()
            // Save cookies received from the server
            override fun saveFromResponse(url: HttpUrl, cookies: MutableList<Cookie>) {
                this.cookies.addAll(cookies)
            }
            // Load cookies for each request
            override fun loadForRequest(url: HttpUrl): MutableList<Cookie> {
                return cookies
            }
        }
        // Create and configure OkHttpClient instance
        val okHttpClient = OkHttpClient.Builder()
            .callTimeout(30, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                // Interceptor to add the JWT token as a cookie in each request
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                val cookieHeader = "WaymateSession=$token"
                requestBuilder.header("Cookie", cookieHeader)
                val request = requestBuilder.build()
                chain.proceed(request)
            }
            .cookieJar(cookieJar)
            .build()
        return okHttpClient
    }
}