package com.example.waymate_mobile.fragments.trip.addNewTrip

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.waymate_mobile.activities.MainActivity
import com.example.waymate_mobile.databinding.FragmentAddNewTripBinding
import com.example.waymate_mobile.dtos.trip.DtoOutputTrip
import com.example.waymate_mobile.repositories.ITripRepository
import com.example.waymate_mobile.utils.RetrofitFactory
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AddNewTripFragment() : Fragment() {
    private lateinit var binding: FragmentAddNewTripBinding
    private val calendar = Calendar.getInstance()
    private lateinit var tripRepository: ITripRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddNewTripBinding.inflate(layoutInflater, container, false)
        // Set up listeners for the date and time pickers and the create button
        setUpListeners()
        return binding.root
    }

    private fun showDatePicker() {
        // Create a DatePickerDialog to select a date
        val datePickerDialog = DatePickerDialog(requireContext(), {DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
            val selectedDate = Calendar.getInstance().apply {
                set(year, monthOfYear, dayOfMonth)
            }
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val formattedDate = dateFormat.format(selectedDate.time)
            binding.etDatePicker.setText(formattedDate)
        },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun showHourPicker() {
        // Create a TimePickerDialog to select a time
        val timePickerDialog = TimePickerDialog(requireContext(), { _, hourOfDay: Int, minute: Int ->
            val selectedTime = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hourOfDay)
                set(Calendar.MINUTE, minute)
            }
            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val formattedTime = timeFormat.format(selectedTime.time)
            binding.etTimePicker.setText(formattedTime)
        },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
        timePickerDialog.show()
    }

    private fun createTrip() {
        // Get JWT token from shared preferences
        val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences("waymate", Context.MODE_PRIVATE)
        val jwtToken = sharedPreferences.getString("jwtToken", "") ?: ""
        // Check if all required fields are filled
        if(binding.etPrice.text.isNullOrEmpty() ||
            binding.etDatePicker.text.isNullOrEmpty() ||
            binding.etTimePicker.text.isNullOrEmpty() ||
            binding.etStartingPoint.text.isNullOrEmpty() ||
            binding.etDestination.text.isNullOrEmpty() ||
            binding.etPlateNumber.text.isNullOrEmpty() ||
            binding.etBrand.text.isNullOrEmpty() ||
            binding.etModel.text.isNullOrEmpty()) {
            Log.d("Error add new trip", "Some fields are empty")
            Toast.makeText(requireContext(),"Some fields are empty", Toast.LENGTH_LONG).show()
        } else {
            // Collect and validate trip data
            val smoke = binding.swtSmoke.isChecked
            val price = binding.etPrice.text.toString().toFloatOrNull() ?: 0.0f
            val luggage = binding.swtLuggage.isChecked
            val petFriendly = binding.swtPet.isChecked
            val dateString = binding.etDatePicker.text.toString()
            val timeString = binding.etTimePicker.text.toString()
            val date: Date? = getDate(dateString, timeString)
            var driverMessage = binding.etMessage.text.toString()
            val airConditioning = binding.swtAir.isChecked
            val cityStartingPoint = binding.etStartingPoint.text.toString()
            val cityDestination = binding.etDestination.text.toString()
            val plateNumber = binding.etPlateNumber.text.toString()
            val brand = binding.etBrand.text.toString()
            val model = binding.etModel.text.toString()

            if(driverMessage.length >=200){
                Log.d("Error add new trip", "Message too long")
                Toast.makeText(requireContext(),"Message too long", Toast.LENGTH_LONG).show()
            } else {
                // Set default driver message if empty
                if(driverMessage.isNullOrEmpty()) {
                    driverMessage = "/"
                }
                // Create a DTO object for the trip
                val dtoTrip = DtoOutputTrip(
                    cityStartingPoint = cityStartingPoint,
                    cityDestination = cityDestination,
                    plateNumber = plateNumber,
                    brand = brand,
                    model = model,
                    date = date!!,
                    price = price,
                    smoke = smoke,
                    luggage = luggage,
                    petFriendly = petFriendly,
                    airConditioning = airConditioning,
                    driverMessage = driverMessage
                )
                // If JWT token is available, make a network request to create the trip
                if (jwtToken != null) {
                    viewLifecycleOwner.lifecycleScope.launch {
                        try {
                            tripRepository = RetrofitFactory.create(jwtToken, ITripRepository::class.java)
                            tripRepository.createTrip(dtoTrip)
                            Log.d("Add new trip", dtoTrip.toString())
                            // Show success message and navigate back
                            Toast.makeText(requireContext(), "Trip created successfully", Toast.LENGTH_SHORT).show()
                            Handler(Looper.getMainLooper()).postDelayed({
                                (requireActivity() as MainActivity).supportFragmentManager.popBackStack()
                            }, 2000)
                        } catch (e: Exception) {
                            Log.e("Error add new trip", "Failed to create trip: ${e.message}")
                        }
                    }
                } else {
                    Log.e("Error add new trip", "JWT token is missing!")
                }
            }
        }
    }

    private fun setUpListeners() {
        // Set up listeners for date and time pickers
        setUpPickerListener(binding.etDatePicker) {
            showDatePicker()
        }
        setUpPickerListener(binding.etTimePicker) {
            showHourPicker()
        }
        // Set up listener for the create trip button
        binding.btnCreateTrip.setOnClickListener {
            createTrip()
        }
    }

    private fun getDate(date: String, time: String): Date? {
        // Combine date and time into a single ISO 8601 formatted string
        val ds = "$date"+"T"+"$time:00"
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val dateParsed: Date? = dateFormat.parse(ds)
        Log.e("ISODate", date)

        return dateParsed
    }
    private fun setUpPickerListener(editText: TextInputEditText, onShowPicker: () -> Unit) {
        // Set up a focus change listener to show the date/time picker when the EditText gains focus
        editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                onShowPicker()
            }
        }
        // Set up a click listener to show the date/time picker when the EditText is clicked
        editText.setOnClickListener {
            onShowPicker()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = AddNewTripFragment()
    }
}