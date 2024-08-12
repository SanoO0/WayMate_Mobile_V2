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

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySigninBinding
    private lateinit var jwtToken: String
    private lateinit var authenticationRepository: IAuthenticationRepository
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize view binding
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize shared preferences and repository
        prefs = getSharedPreferences("waymate", Context.MODE_PRIVATE)
        jwtToken = prefs.getString("jwtToken", "") ?: ""
        authenticationRepository = RetrofitFactory.create(jwtToken, IAuthenticationRepository::class.java)

        // Set up sign-in button click listener
        binding.btnSignIn.setOnClickListener {
            submitForm(
                binding.etLogin.text.toString(),
                binding.etPassword.text.toString(),
                true
            )
        }
    }

    // Submits the sign-in form by sending a request to the authentication repository
    private fun submitForm(email: String, password: String, stay: Boolean) {
        lifecycleScope.launch {
            try {
                // Attempt to sign in with the provided email and password
                authenticationRepository.signIn(DtoOutputSigIn(email, password, stay)).let { response ->
                    if (response.isSuccessful) {
                        Log.d("Sign In", "Successful")
                        // If sign-in is successful, extract and save the JWT token
                        response.headers().get("Set-Cookie")?.let { cookieHeader ->
                            extractTokenFromCookie(cookieHeader)?.let { token ->
                                updateTokenInPreferences(token)
                            }
                        }
                        // Navigate to the main activity
                        navigateToMainActivity()
                    } else {
                        // Handle unsuccessful sign-in attempts
                        Log.e("ErrorAuth", "Error Auth: ${response.message()}")
                        Toast.makeText(this@SignInActivity, "Email or Password incorrect", Toast.LENGTH_SHORT).show()
                        clearFormFields()
                    }
                }
            } catch (e: Exception) {
                // Log any errors that occur during the sign-in process
                Log.e("ErrorDb", "Error DB: ${e.message}", e)
            }
        }
    }

    // Extracts the JWT token from the "Set-Cookie" header
    private fun extractTokenFromCookie(cookieHeader: String): String? = Pattern.compile("WaymateSession=([^;]+)").matcher(cookieHeader).takeIf { it.find() }?.group(1)

    // Updates the JWT token in shared preferences
    private fun updateTokenInPreferences(newToken: String) {
        getSharedPreferences("waymate", MODE_PRIVATE).edit().apply {
            putString("jwtToken", newToken)
            apply()
        }
    }

    // Navigates to the main activity
    private fun navigateToMainActivity() {
        startActivity(Intent(this@SignInActivity, MainActivity::class.java))
    }

    // Clears the form fields (email and password)
    private fun clearFormFields() {
        binding.etLogin.text?.clear()
        binding.etPassword.text?.clear()
    }
}