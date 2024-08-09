package com.example.waymate_mobile.fragments.trip.addNewTrip

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.waymate_mobile.R
import com.example.waymate_mobile.databinding.FragmentAddNewTripBinding
import com.example.waymate_mobile.databinding.FragmentShowTravelBinding
import com.example.waymate_mobile.fragments.showTravel.ShowTravelFragment

class AddNewTripFragment : Fragment() {
    private lateinit var binding: FragmentAddNewTripBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddNewTripBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = AddNewTripFragment()
    }
}