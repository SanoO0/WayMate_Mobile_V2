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
import com.example.waymate_mobile.fragments.showTravel.ShowTravelFragment
import com.example.waymate_mobile.repositories.IAuthenticationRepository
import com.example.waymate_mobile.repositories.ITripRepository
import com.example.waymate_mobile.utils.RetrofitFactory
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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
        binding = FragmentAddNewTripBinding.inflate(layoutInflater, container, false)
        setUpListeners()
        return binding.root
    }

    private fun showDatePicker() {
        val datePickerDialog = DatePickerDialog(requireContext(), {DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, monthOfYear, dayOfMonth)
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
        val timePickerDialog = TimePickerDialog(requireContext(), { _, hourOfDay: Int, minute: Int ->
            val selectedTime = Calendar.getInstance()
            selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
            selectedTime.set(Calendar.MINUTE, minute)
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
        val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences("waymate", Context.MODE_PRIVATE)
        val jwtToken = sharedPreferences.getString("jwtToken", "") ?: ""
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
                Log.d("Error add new trip", "Message To Long")
                Toast.makeText(requireContext(),"Message To Long", Toast.LENGTH_LONG).show()
            } else {
                if(driverMessage.isNullOrEmpty()) {
                    driverMessage = "/"
                }
                val dtoTrip = DtoOutputTrip(
                    cityStartingPoint = cityStartingPoint,
                    cityDestination = cityDestination,
                    plateNumber = plateNumber,
                    brand = brand,
                    model = model,
                    date = date!!,  // date est maintenant au format ISO 8601
                    price = price as Float,
                    smoke = smoke,
                    luggage = luggage,
                    petFriendly = petFriendly,
                    airConditioning = airConditioning,
                    driverMessage = driverMessage
                )
                if (jwtToken != null) {
                    viewLifecycleOwner.lifecycleScope.launch {
                        try {
                            tripRepository = RetrofitFactory.create(jwtToken, ITripRepository::class.java)
                            tripRepository.createTrip(dtoTrip)
                            Log.d("Add new trip", dtoTrip.toString())

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
        setUpPickerListener(binding.etDatePicker) {
            showDatePicker()
        }
        setUpPickerListener(binding.etTimePicker) {
            showHourPicker()
        }
        binding.btnCreateTrip.setOnClickListener {
            createTrip()
        }
    }

    private fun getDate(date:String,time:String): Date? {
        val ds = "$date"+"T"+"$time:00"
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val date: Date? = dateFormat.parse(ds)
        Log.e("ISODate", date.toString())

        return date
    }
    private fun setUpPickerListener(editText: TextInputEditText, onShowPicker: () -> Unit) {
        editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                onShowPicker()
            }
        }
        editText.setOnClickListener {
            onShowPicker()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = AddNewTripFragment()
    }
}