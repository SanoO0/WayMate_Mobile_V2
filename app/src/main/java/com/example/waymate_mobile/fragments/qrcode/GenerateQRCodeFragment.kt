package com.example.waymate_mobile.fragments.qrcode

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import com.example.waymate_mobile.databinding.FragmentGenerateQRCodeBinding
import com.example.waymate_mobile.dtos.trip.DtoInputTrip
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class GenerateQRCodeFragment() : Fragment() {
    private lateinit var binding: FragmentGenerateQRCodeBinding
    private var dataQRCode: String = ""
    private var code: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentGenerateQRCodeBinding.inflate(layoutInflater, container, false)
        // Retrieve the JSON string representing DtoInputTrip from fragment arguments
        dataQRCode = arguments?.getString("dataQRcodeDriver") ?: ""
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Generate the QR code and set up listeners once the view is created
        generateQRCode()
        setUpListeners()
    }


    private fun generateQRCode() {
        // Check if there is data to generate QR Code
        if (dataQRCode.isEmpty()) {
            Toast.makeText(requireContext(), "No data available to generate QR Code", Toast.LENGTH_SHORT).show()
            return
        }
        // Create a QRGEncoder to generate the QR code bitmap from the dataQRCode string
        val encoder = QRGEncoder(dataQRCode, null, QRGContents.Type.TEXT, 800)
        encoder.setColorBlack(Color.TRANSPARENT);
        encoder.setColorWhite(Color.BLACK);
        binding.qrcodeGenerator.setImageBitmap(encoder.bitmap)

        try {
            // Parse the JSON string into a DtoInputTrip object using Gson
            val dtoTrip = Gson().fromJson(dataQRCode, DtoInputTrip::class.java)
            // Generate the code based on trip details
            generateCode(dtoTrip)
            // Format and display trip details
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val formattedDate = dateFormat.format(dtoTrip.date)
            binding.tvDataDate.text = formattedDate
            binding.tvDataDepart.text = dtoTrip.cityStartingPoint
            binding.tvDataDestination.text = dtoTrip.cityDestination
            binding.tvToday.text = getCurrentDate()

        } catch (e: JsonSyntaxException) {
            // Handle JSON parsing errors
            binding.tvDataDate.text = "Error parsing trip data"
            e.printStackTrace()
        }
    }

    private fun setUpListeners() {
        // Set up a listener for the validation button
        binding.btnCode.setOnClickListener {
            val etCode: String = binding.etCode.text.toString()
            Log.e("Entered Code", etCode)
            // Check if the entered code matches the generated code
            if(etCode == code) {
                binding.etPassengerValid.text = "Valid code - Passenger accepted"
                Toast.makeText(context, "Code valid", Toast.LENGTH_SHORT).show()
            } else {
                binding.etPassengerValid.text = "Invalid code - Passenger refused"
                Toast.makeText(context, "Code invalid", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun generateCode(dto: DtoInputTrip) {
        // Generate a unique code based on trip details
        val builder: StringBuilder = StringBuilder()
        builder.append(dto.plateNumber[0])
            .append(dto.cityStartingPoint[2])
            .append(dto.cityDestination[1])
            .append(dto.model[0])
            .append(dto.brand[0])

        code = builder.toString()
        Log.e("Generated Code", code)
    }

    private fun getCurrentDate():String{
        // Get the current date and time in the specified format
        val sdf = SimpleDateFormat("dd/MM/yyyy - HH:mm", Locale.getDefault())
        return sdf.format(Date())
    }

    companion object {
        fun newInstance(dataQRcodeDriver: DtoInputTrip): GenerateQRCodeFragment {
            // Create a new instance of the fragment with the provided DtoInputTrip data
            val fragment = GenerateQRCodeFragment()
            val json = Gson().toJson(dataQRcodeDriver)
            val args = Bundle().apply {
                putString("dataQRcodeDriver", json)
            }
            fragment.arguments = args
            return fragment
        }
    }
}