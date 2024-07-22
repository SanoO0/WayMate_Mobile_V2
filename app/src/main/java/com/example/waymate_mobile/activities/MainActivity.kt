package com.example.waymate_mobile.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.waymate_mobile.R
import com.example.waymate_mobile.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!isTokenPresent()) {
            startActivity(Intent(this, SigninActivity::class.java))
            finish()
        }

        binding.Disconnection.setOnClickListener {
            signOut()
        }
    }

    private fun isTokenPresent(): Boolean = getSharedPreferences("waymate", MODE_PRIVATE).getString("jwtToken", null) != null

    fun signOut() {
        val prefs = getSharedPreferences("waymate", MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString("jwtToken", null)
        editor.apply()

        if (!isTokenPresent()) {
            startActivity(Intent(this, SigninActivity::class.java))
            finish()
        }
    }
}