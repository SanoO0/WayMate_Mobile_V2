package com.example.waymate_mobile.fragments.trip.moreDetails

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.waymate_mobile.databinding.FragmentMoreDetailsBinding
import com.example.waymate_mobile.dtos.trip.DtoInputTrip
import com.example.waymate_mobile.repositories.HereGeocodingService
import com.example.waymate_mobile.repositories.HereRoutingService
import com.example.waymate_mobile.repositories.IUserRepository
import com.example.waymate_mobile.utils.RetrofitFactory
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MoreDetailsFragment : Fragment() {
    private lateinit var binding: FragmentMoreDetailsBinding
    private var dataMoreDetails: String = ""
    private val apiKey = "_c0cPaKp1zjErUdCtmOLuSccwO8IlQX4HRF6YYC0O2Y"
    private lateinit var dtoTrip: DtoInputTrip
    private lateinit var userRepository: IUserRepository
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMoreDetailsBinding.inflate(layoutInflater, container, false)
        dataMoreDetails = arguments?.getString("dataMoreDetailsPassenger") ?: ""
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEditText()
        initEditDriverInfo()
    }
    private fun calculateTravelTime(startCity: String, endCity: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://geocode.search.hereapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val geocodingService = retrofit.create(HereGeocodingService::class.java)
        val routingService = retrofit.newBuilder()
            .baseUrl("https://router.hereapi.com/")
            .build()
            .create(HereRoutingService::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val startResponse = geocodingService.geocodeAddress(apiKey, startCity)
                val endResponse = geocodingService.geocodeAddress(apiKey, endCity)

                if (startResponse.isSuccessful && endResponse.isSuccessful) {
                    val startPosition = startResponse.body()?.items?.firstOrNull()?.position
                    val endPosition = endResponse.body()?.items?.firstOrNull()?.position

                    if (startPosition != null && endPosition != null) {
                        val origin = "${startPosition.lat},${startPosition.lng}"
                        val destination = "${endPosition.lat},${endPosition.lng}"

                        val routeResponse = routingService.getRoute(apiKey, origin = origin, destination = destination)
                        val duration = routeResponse.body()?.routes?.firstOrNull()
                            ?.sections?.firstOrNull()?.summary?.duration
                        if (duration != null) {
                            val hours = duration / 3600
                            val minutes = (duration % 3600) / 60
                            val arrivalDate = Date(dtoTrip.date.time + duration * 1000)
                            val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                            val date = dateFormat.format(arrivalDate)
                            withContext(Dispatchers.Main) {
                                binding.tvTravelTime.text = "Estimated travel time: $hours hours and $minutes minutes"
                                binding.tvTimeArrive.text = "Estimated arrival time: $date"
                            }
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        binding.tvTravelTime.text = "Error fetching travel time"
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    binding.tvTravelTime.text = "Exception: ${e.message}"
                }
            }
        }
    }

    private fun initEditText() {
        val gson = Gson()
        dtoTrip = gson.fromJson(dataMoreDetails, DtoInputTrip::class.java)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val date = dateFormat.format(dtoTrip.date)

        binding.tvCityStart.text = dtoTrip.cityStartingPoint
        binding.tvCityDest.text = dtoTrip.cityDestination
        binding.tvDate.text = date
        binding.tvPrice.text = dtoTrip.price.toString()
        binding.tvPlateNumber.text = dtoTrip.plateNumber
        binding.tvCarBrand.text = dtoTrip.brand
        binding.tvCarModel.text = dtoTrip.model
        binding.tvAir.text = verifyBoolean(dtoTrip.airConditioning)
        binding.tvSmoke.text = verifyBoolean(dtoTrip.smoke)
        binding.tvPet.text = verifyBoolean(dtoTrip.petFriendly)
        binding.tvLuggage.text = verifyBoolean(dtoTrip.luggage)
        binding.tvMessage.text = dtoTrip.driverMessage

        calculateTravelTime(dtoTrip.cityStartingPoint,dtoTrip.cityDestination)
    }

    private fun initEditDriverInfo() {
        val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences("waymate", Context.MODE_PRIVATE)
        val jwtToken = sharedPreferences.getString("jwtToken", "") ?: ""

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                userRepository = RetrofitFactory.create(jwtToken, IUserRepository::class.java)
                val driverInfo = userRepository.getUserDataById(dtoTrip.idDriver)
                Log.e("driverINFO", driverInfo.toString())
                binding.tvName.text = driverInfo.lastName
                binding.tvPhone.text = driverInfo.phoneNumber
                binding.tvMail.text = driverInfo.email
                binding.tvGender.text = driverInfo.gender

            } catch (e: Exception) {
                Log.e("Error", "Failed to get user: ${e.message}")
            }
        }
    }

    private fun verifyBoolean(boolean: Boolean): String {
        if(boolean) {
            return "Yes"
        } else {
            return "No"
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(dataMoreDetails: DtoInputTrip): MoreDetailsFragment {
            val fragment = MoreDetailsFragment()
            val gson = Gson()
            val json = gson.toJson(dataMoreDetails)
            val args = Bundle()
            args.putString("dataMoreDetailsPassenger", json)
            fragment.arguments = args
            return fragment
        }
    }
}