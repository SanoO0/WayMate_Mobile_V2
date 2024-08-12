package com.example.waymate_mobile.services

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.waymate_mobile.dtos.authentication.DtoInputAuthentication
import com.example.waymate_mobile.repositories.IAuthenticationRepository
import com.example.waymate_mobile.utils.RetrofitFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class UserTypeService(private val context: Context) {
    private lateinit var authenticationRepository: IAuthenticationRepository
    lateinit var prefs: SharedPreferences
    // Suspend function to retrieve the user type from the server
    suspend fun getUserType(): String = suspendCoroutine { continuation ->
        // Initialize shared preferences
        prefs = context.getSharedPreferences("waymate", Context.MODE_PRIVATE)
        val jwtToken = prefs.getString("jwtToken", "") ?: ""
        // Initialize the authentication repository with the JWT token
        authenticationRepository =
            RetrofitFactory.create(jwtToken, IAuthenticationRepository::class.java)
        // Make a network call to get the user type
        val call = authenticationRepository.getUserType()
        call.enqueue(object : Callback<DtoInputAuthentication> {
            // Handle the response from the server
            override fun onResponse(
                call: Call<DtoInputAuthentication>,
                response: Response<DtoInputAuthentication>
            ) {
                if (response.isSuccessful) {
                    // Resume the coroutine with the user type if the response is successful
                    val userType = response.body()?.userType
                    userType?.let {
                        continuation.resume(userType)
                    }
                } else {
                    // Log the failure message if the response is not successful
                    val message = "Failed : ${response.message()}"
                    Log.d("Failure", message)
                }
            }
            // Handle failure of the network call
            override fun onFailure(call: Call<DtoInputAuthentication>, t: Throwable) {
                // Log the error message if the network request fails
                val message = "Database Failure: ${t.message}"
                Log.e("DbFailure", message, t)
            }
        })
    }
}