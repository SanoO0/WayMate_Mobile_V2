package com.example.waymate_mobile.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.waymate_mobile.databinding.ActivitySigninBinding
import com.example.waymate_mobile.dtos.authentication.DtoOutputSigIn
import com.example.waymate_mobile.repositories.IAuthenticationRepository
import com.example.waymate_mobile.utils.RetrofitFactory
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class SigninActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySigninBinding
    private lateinit var jwtToken: String
    private lateinit var authenticationRepository: IAuthenticationRepository
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefs = getSharedPreferences("waymate", Context.MODE_PRIVATE)
        jwtToken = prefs.getString("jwtToken", "") ?: ""
        authenticationRepository = RetrofitFactory.create(jwtToken, IAuthenticationRepository::class.java)

        binding.btnSignIn.setOnClickListener {
            submitForm(
                binding.etLogin.text.toString(),
                binding.etPassword.text.toString()
            )
        }
    }

    private fun submitForm(email: String, password: String) {
        lifecycleScope.launch {
            try {
                authenticationRepository.signIn(DtoOutputSigIn(email, password)).let { response ->
                    if (response.isSuccessful) {
                        response.headers().get("Set-Cookie")?.let { cookieHeader ->
                            extractTokenFromCookie(cookieHeader)?.let { token ->
                                updateTokenInPreferences(token)
                            }

                        }
                        if(authenticationRepository.testTypeUserPassenger().isSuccessful){
                            Log.d("Test User", "Passenger")
                        } else if (authenticationRepository.testTypeUserDriver().isSuccessful) {
                            Log.d("Test User", "Driver")
                        } else if (authenticationRepository.testTypeUserAdmin().isSuccessful) {
                            Log.d("Test User", "Admin")
                        }
                        navigateToMainActivity()

                    } else {
                        Toast.makeText(this@SigninActivity, "Username or Password incorrect", Toast.LENGTH_SHORT).show()
                        binding.etLogin.text?.clear()
                        binding.etPassword.text?.clear()
                      Log.d("ErrorAuth", "Error Auth: ${response.message()}")
                    }
                }
            } catch (e: Exception) {
                Log.e("ErrorDb", "Error DB: ${e.message}", e)
            }
        }
    }

    private fun extractTokenFromCookie(cookieHeader: String): String? = Pattern.compile("WayMateToken=([^;]+)").matcher(cookieHeader).takeIf { it.find() }?.group(1)
    private fun updateTokenInPreferences(newToken: String) {
        getSharedPreferences("waymate", MODE_PRIVATE).edit().apply {
            putString("jwtToken", newToken)
            apply()
        }
    }

    private fun navigateToMainActivity() {
        startActivity(Intent(this@SigninActivity, MainActivity::class.java))
    }
}