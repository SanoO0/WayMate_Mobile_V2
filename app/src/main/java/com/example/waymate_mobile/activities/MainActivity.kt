package com.example.waymate_mobile.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.waymate_mobile.R
import com.example.waymate_mobile.databinding.ActivityMainBinding
import com.example.waymate_mobile.fragments.menu.MainMenuDriverFragment
import com.example.waymate_mobile.services.UserTypeService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var userTypeService: UserTypeService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!isTokenPresent()) {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }
        userTypeTest()
    }

    private fun showMainMenuDriver() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView_mainActivity, MainMenuDriverFragment.newInstance(), "MainMenuDriverFragment")
            .commit()
        binding.topMenuBar.menu.getItem(0).setIcon(0)
        binding.topMenuBar.menu.getItem(0).setEnabled(false)
        binding.topMenuBar.menu.getItem(0).title = ""
    }

    private fun isTokenPresent(): Boolean = getSharedPreferences("waymate", MODE_PRIVATE).getString("jwtToken", null) != null

    fun signOut() {
        val prefs = getSharedPreferences("waymate", MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString("jwtToken", null)
        editor.apply()

        if (!isTokenPresent()) {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }
    }

    private fun userTypeTest(){
        userTypeService = UserTypeService(this)
        CoroutineScope(Dispatchers.Main).launch {
            val userType = userTypeService.getUserType()
            if(userType == "Driver") {
                showMainMenuDriver()
            } else if(userType == "Passenger") {

            } else if(userType == "Admin") {

            } else {

            }
        }
    }
}