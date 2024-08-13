package com.example.waymate_mobile.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.waymate_mobile.R
import com.example.waymate_mobile.databinding.ActivityMainBinding
import com.example.waymate_mobile.dtos.trip.DtoInputTrip
import com.example.waymate_mobile.fragments.account.DriverAccountFragment
import com.example.waymate_mobile.fragments.account.PassengerAccountFragment
import com.example.waymate_mobile.fragments.menu.MainMenuDriverFragment
import com.example.waymate_mobile.fragments.menu.MainMenuPassengerFragment
import com.example.waymate_mobile.fragments.qrcode.GenerateQRCodeFragment
import com.example.waymate_mobile.fragments.showTravel.ShowTravelFragment
import com.example.waymate_mobile.fragments.trip.addNewTrip.AddNewTripFragment
import com.example.waymate_mobile.fragments.trip.modifyTrip.ModifyTripFragment
import com.example.waymate_mobile.fragments.trip.moreDetails.MoreDetailsFragment
import com.example.waymate_mobile.services.UserTypeService
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.sign

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var userTypeService: UserTypeService
    var userType = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Check if the authentication token is present
        if (!isTokenPresent()) {
            showSignInPage()
        }
        userTypeTest()
        setUpListeners()
    }

    private fun showSignInPage() {
        startActivity(Intent(this, SignInActivity::class.java))
        finish()
    }

    private fun showMainMenuDriver() {
        replaceFragment(MainMenuDriverFragment.newInstance())
        changeTopMenu(0, false, "")
    }

    private fun showMainMenuPassenger() {
        replaceFragment(MainMenuPassengerFragment.newInstance())
        changeTopMenu(0, false, "")
    }

    fun showProfileDriver(){
        replaceFragment(DriverAccountFragment.newInstance())
        changeTopMenu(R.drawable.ic_arrow_back_24, true, "My Profile - Driver")
    }

    fun showProfilePassenger(){
        replaceFragment(PassengerAccountFragment.newInstance())
        changeTopMenu(R.drawable.ic_arrow_back_24, true, "My Profile - Passenger")
    }

    fun showDriverTravel() {
        replaceFragment(ShowTravelFragment.newInstance())
        changeTopMenu(R.drawable.ic_arrow_back_24, true, "My Travel - Driver")
    }

    fun showGenerateQRCode(dataQRcodeDriver: DtoInputTrip){
        replaceFragment(GenerateQRCodeFragment.newInstance(dataQRcodeDriver))
        changeTopMenu(R.drawable.ic_arrow_back_24, true, "Generate QRCode - Driver")

    }

    fun showPassengerTravel() {
        replaceFragment(ShowTravelFragment.newInstance())
        changeTopMenu(R.drawable.ic_arrow_back_24, true, "My Booking - Passenger")
    }

    fun showAddNewTrip() {
        replaceFragment(AddNewTripFragment.newInstance())
        changeTopMenu(R.drawable.ic_arrow_back_24, true, "Add New Trip - Driver")
    }

    fun showModifyTrip(dataModifyTrip: DtoInputTrip) {
        replaceFragment(ModifyTripFragment.newInstance(dataModifyTrip))
        changeTopMenu(R.drawable.ic_arrow_back_24, true, "Modify Trip - Driver")
    }

    fun showScanQrCode(dataScanTrip: DtoInputTrip) {
        val gson = Gson()
        val json = gson.toJson(dataScanTrip)
        val intent = Intent(this, QRScanActivity::class.java).apply {
            putExtra("TRIP_DATA_JSON", json)
        }
        startActivity(intent)
        finish()
    }

    fun showPassengerTripMoreDetails(dataMoreDetailsTrip: DtoInputTrip) {
        replaceFragment(MoreDetailsFragment.newInstance(dataMoreDetailsTrip))
        changeTopMenu(R.drawable.ic_arrow_back_24, true, "My Booking - Passenger")
    }

    private fun replaceFragment(fragment: androidx.fragment.app.Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView_mainActivity, fragment)
            .addToBackStack(null)
            .commit()
    }

    fun changeTopMenu(icon: Int, enable: Boolean, title: String) {
        binding.topMenuBar.menu.getItem(0).apply {
            setIcon(icon)
            isEnabled = enable
            this.title = title
        }
    }

    private fun isTokenPresent(): Boolean = getSharedPreferences("waymate", MODE_PRIVATE).getString("jwtToken", null) != null

    fun signOut() {
        val prefs = getSharedPreferences("waymate", MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString("jwtToken", null)
        editor.apply()
        if (!isTokenPresent()) {
            showSignInPage()
        }
    }

    //Get userType
    private fun userTypeTest(){
        userTypeService = UserTypeService(this)
        CoroutineScope(Dispatchers.Main).launch {
            userType = userTypeService.getUserType()
            if(userType == "Driver") {
                showMainMenuDriver()
            } else if(userType == "Passenger") {
                showMainMenuPassenger()
            } else {
                showSignInPage()
            }
        }
    }

    //Go back to previous fragment
    private fun setUpListeners() {
        binding.topMenuBar.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.back -> {
                    supportFragmentManager.popBackStack()
                }
            }
            true
        }
    }
}