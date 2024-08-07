package com.example.waymate_mobile.fragments.showTravel.passenger

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.waymate_mobile.dtos.trip.DtoInputTrip
import com.example.waymate_mobile.repositories.ITripRepository
import com.example.waymate_mobile.utils.RetrofitFactory
import kotlinx.coroutines.launch

class ShowPassengerTravelManagerViewModel(private val jwtToken: String) : ViewModel() {
    val mutableTripLiveData: MutableLiveData<List<DtoInputTrip>?> = MutableLiveData()
    val mutableCount: MutableLiveData<Int> = MutableLiveData()
    private val tripRepository = RetrofitFactory.create(jwtToken, ITripRepository::class.java)
    var showCount = 5

    fun startGetAllTrips() {
        viewModelScope.launch {
            try {
                val response = tripRepository.getAllTripPassenger(showCount)
                val nbTrip = response.size
                if (nbTrip != null) {
                    mutableCount.postValue(nbTrip)
                    val slicedTrips = response.slice(0 until nbTrip)
                    mutableTripLiveData.postValue(null)
                    mutableTripLiveData.postValue(slicedTrips)
                }
            } catch (e: Exception) {
                Log.e("Echec", e.message.toString())
            }
        }
    }
}