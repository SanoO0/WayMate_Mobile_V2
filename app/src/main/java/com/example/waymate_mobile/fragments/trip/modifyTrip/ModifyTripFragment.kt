package com.example.waymate_mobile.fragments.trip.modifyTrip

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.waymate_mobile.activities.MainActivity
import com.example.waymate_mobile.databinding.FragmentModifyTripBinding
import com.example.waymate_mobile.dtos.trip.DtoInputTrip
import com.example.waymate_mobile.dtos.trip.DtoOutputTrip
import com.example.waymate_mobile.repositories.ITripRepository
import com.example.waymate_mobile.utils.RetrofitFactory
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ModifyTripFragment : Fragment() {
    private lateinit var binding: FragmentModifyTripBinding
    private var dataModify: String = ""
    private val calendar = Calendar.getInstance()
    private lateinit var tripRepository: ITripRepository
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentModifyTripBinding.inflate(layoutInflater, container, false)
        // Retrieve and store the data to modify from arguments
        val dataModifyTrip = arguments?.getString("dataModifyTrip") ?: ""
        dataModify = dataModifyTrip
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize the EditText fields with the existing trip data
        initEditText()
        // Set up listeners for date/time pickers and the modify button
        setUpListeners()
    }

    private fun initEditText() {
        // Deserialize the JSON data into a DtoInputTrip object
        val dtoTrip: DtoInputTrip = Gson().fromJson(dataModify, DtoInputTrip::class.java)
        // Populate the fields with the data from dtoTrip
        binding.etStartingPoint.setText(dtoTrip.cityStartingPoint)
        binding.etDestination.setText(dtoTrip.cityDestination)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        binding.etDatePicker.setText(dateFormat.format(dtoTrip.date))
        val hourFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        binding.etTimePicker.setText(hourFormat.format(dtoTrip.date))
        binding.etPrice.setText(dtoTrip.price.toString())
        binding.etPlateNumber.setText(dtoTrip.plateNumber)
        binding.etBrand.setText(dtoTrip.brand)
        binding.etModel.setText(dtoTrip.model)
        binding.swtAir.isChecked = dtoTrip.airConditioning
        binding.swtSmoke.isChecked = dtoTrip.smoke
        binding.swtPet.isChecked = dtoTrip.petFriendly
        binding.swtLuggage.isChecked = dtoTrip.luggage
        binding.etMessage.setText(dtoTrip.driverMessage)
        Log.d("dto modify", dtoTrip.toString())
    }

    private fun updateTrip() {
        // Retrieve JWT token from shared preferences
        val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences("waymate", Context.MODE_PRIVATE)
        val jwtToken = sharedPreferences.getString("jwtToken", "") ?: ""
        val dtoTripData: DtoInputTrip = Gson().fromJson(dataModify, DtoInputTrip::class.java)
        // Validate that all required fields are filled
        if(binding.etPrice.text.isNullOrEmpty() ||
            binding.etDatePicker.text.isNullOrEmpty() ||
            binding.etTimePicker.text.isNullOrEmpty() ||
            binding.etStartingPoint.text.isNullOrEmpty() ||
            binding.etDestination.text.isNullOrEmpty() ||
            binding.etPlateNumber.text.isNullOrEmpty() ||
            binding.etBrand.text.isNullOrEmpty() ||
            binding.etModel.text.isNullOrEmpty()) {
            Log.d("Error modify trip", "Some fields are empty")
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
                Log.d("Error modify trip", "Message too long")
                Toast.makeText(requireContext(),"Message too long", Toast.LENGTH_LONG).show()
            } else {
                // Set default driver message if empty
                if (driverMessage.isNullOrEmpty()) {
                    driverMessage = "/"
                }
                // Create a DTO object for the updated trip
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
                if (jwtToken != null) {
                    viewLifecycleOwner.lifecycleScope.launch {
                        try {
                            tripRepository = RetrofitFactory.create(jwtToken, ITripRepository::class.java)
                            tripRepository.updateTrip(dtoTripData.id ,dtoTrip)
                            Log.d("Update trip", dtoTrip.toString())
                            // Show success message and navigate back
                            Toast.makeText(requireContext(), "Trip updated successfully", Toast.LENGTH_SHORT).show()
                            Handler(Looper.getMainLooper()).postDelayed({
                                (requireActivity() as MainActivity).supportFragmentManager.popBackStack()
                            }, 2000)
                        } catch (e: Exception) {
                            Log.e("Error modify trip", "Failed to update trip: ${e.message}")
                        }
                    }
                } else {
                    Log.e("Error modify trip", "JWT token is missing!")
                }
            }
        }
    }

    private fun showDatePicker() {
        // Create and show a DatePickerDialog
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
        // Create and show a TimePickerDialog
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

    private fun setUpListeners() {
        // Set up listeners for date/time pickers
        setUpPickerListener(binding.etDatePicker) {
            showDatePicker()
        }
        setUpPickerListener(binding.etTimePicker) {
            showHourPicker()
        }
        // Set up listener for the modify button
        binding.btnModifyTrip.setOnClickListener {
            updateTrip()
        }
    }

    private fun setUpPickerListener(editText: TextInputEditText, onShowPicker: () -> Unit) {
        // Show the picker when the EditText gains focus or is clicked
        editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                onShowPicker()
            }
        }
        editText.setOnClickListener {
            onShowPicker()
        }
    }

    private fun getDate(date:String,time:String): Date? {
        // Combine date and time into a single ISO 8601 formatted string
        val ds = "$date"+"T"+"$time:00"
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val dateParsed: Date? = dateFormat.parse(ds)
        Log.e("ISODate", dateParsed.toString())

        return dateParsed
    }

    companion object {
        @JvmStatic
        fun newInstance(dataModifyTrip: DtoInputTrip): ModifyTripFragment {
            val fragment = ModifyTripFragment()
            val gson = Gson()
            val json = gson.toJson(dataModifyTrip)
            val args = Bundle()
            args.putString("dataModifyTrip", json)
            fragment.arguments = args
            return fragment
        }
    }
}