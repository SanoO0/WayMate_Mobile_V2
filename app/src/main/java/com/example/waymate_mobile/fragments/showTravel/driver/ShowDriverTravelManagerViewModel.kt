package com.example.waymate_mobile.fragments.showTravel.driver

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.waymate_mobile.dtos.trip.DtoInputTrip
import com.example.waymate_mobile.repositories.ITripRepository
import com.example.waymate_mobile.utils.RetrofitFactory
import kotlinx.coroutines.launch

class ShowDriverTravelManagerViewModel(private val jwtToken:String) : ViewModel() {
    val mutableTripLivaData: MutableLiveData<List<DtoInputTrip>?> = MutableLiveData()
    val mutableCount: MutableLiveData<Int> = MutableLiveData()
    private val tripRepository = RetrofitFactory.create(jwtToken, ITripRepository::class.java)
    var showCount = 5

    fun startGetAllTrips() {
        viewModelScope.launch {
            try {
                val response = tripRepository.getAllTrip(showCount)
                val nbTrip = response.size
                if (nbTrip != null) {
                    mutableCount.postValue(nbTrip)
                    val slicedTrips = response.slice(0 until nbTrip)
                    mutableTripLivaData.postValue(null)
                    mutableTripLivaData.postValue(slicedTrips)
                }
            } catch (e: Exception) {
                Log.e("Echec", e.message.toString())
            }
        }
    }
}