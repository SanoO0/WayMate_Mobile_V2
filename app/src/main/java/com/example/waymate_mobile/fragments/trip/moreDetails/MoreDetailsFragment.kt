package com.example.waymate_mobile.fragments.trip.moreDetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.waymate_mobile.R
import com.example.waymate_mobile.databinding.FragmentMoreDetailsBinding
import com.example.waymate_mobile.dtos.trip.DtoInputTrip
import com.example.waymate_mobile.fragments.qrcode.GenerateQRCodeFragment
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

class MoreDetailsFragment : Fragment() {
    private lateinit var binding: FragmentMoreDetailsBinding
    private var dataMoreDetails: String = ""
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

    }

    private fun initEditText() {
        val gson = Gson()
        val dtoTrip: DtoInputTrip = gson.fromJson(dataMoreDetails, DtoInputTrip::class.java)

        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
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