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

    suspend fun getUserType(): String = suspendCoroutine { continuation ->
        prefs = context.getSharedPreferences("waymate", Context.MODE_PRIVATE)
        val jwtToken = prefs.getString("jwtToken", "") ?: ""
        authenticationRepository = RetrofitFactory.create(jwtToken, IAuthenticationRepository::class.java)
        val call = authenticationRepository.getUserType()
        call.enqueue(object : Callback<DtoInputAuthentication> {
            override fun onResponse(call: Call<DtoInputAuthentication>, response: Response<DtoInputAuthentication>) {
                if (response.isSuccessful) {
                    val userType = response.body()?.userType
                    userType?.let {
                        continuation.resume(userType)
                    }
                } else {
                    val message = "echec : ${response.message()}"
                    Log.d("Echec", message)
                }
            }
            override fun onFailure(call: Call<DtoInputAuthentication>, t:Throwable) {
                val message = "Echec DB: ${t.message}"
                Log.e("EchecDb", message, t)
            }
        })
    }
}