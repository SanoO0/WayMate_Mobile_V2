package com.example.waymate_mobile.fragments.trip.addNewTrip

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.waymate_mobile.R
import com.example.waymate_mobile.databinding.FragmentAddNewTripBinding
import com.example.waymate_mobile.databinding.FragmentShowTravelBinding
import com.example.waymate_mobile.fragments.showTravel.ShowTravelFragment
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddNewTripFragment : Fragment() {
    private lateinit var binding: FragmentAddNewTripBinding
    private val calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddNewTripBinding.inflate(layoutInflater, container, false)

        setUpListeners()

        return binding.root
    }


    private fun showDatePicker() {
        val datePickerDialog = DatePickerDialog(requireContext(), {DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, monthOfYear, dayOfMonth)
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val formattedDate = dateFormat.format(selectedDate.time)
            binding.etDatePicker.setText(formattedDate)
        },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    fun setUpListeners() {
        binding.etDatePicker.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                showDatePicker()
            }
        }
        binding.etDatePicker.setOnClickListener {
            showDatePicker()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = AddNewTripFragment()
    }
}