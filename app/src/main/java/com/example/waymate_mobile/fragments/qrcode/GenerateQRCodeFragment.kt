package com.example.waymate_mobile.fragments.qrcode

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.annotation.RequiresApi
import com.example.waymate_mobile.R
import com.example.waymate_mobile.databinding.FragmentGenerateQRCodeBinding
import com.example.waymate_mobile.dtos.trip.DtoInputTrip
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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
        binding = FragmentGenerateQRCodeBinding.inflate(layoutInflater, container, false)
        val dataQRcodeDriver = arguments?.getString("dataQRcodeDriver") ?: ""
        dataQRCode = dataQRcodeDriver
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        generateQRCode()
        setUpListeners()
    }


    private fun generateQRCode() {
        val text = dataQRCode
        val encoder = QRGEncoder(text, null, QRGContents.Type.TEXT, 800)
        encoder.setColorBlack(Color.TRANSPARENT);
        encoder.setColorWhite(Color.BLACK);
        binding.qrcodeGenerator.setImageBitmap(encoder.bitmap)

        try {
            val gson = Gson()
            val dtoTrip: DtoInputTrip = gson.fromJson(text, DtoInputTrip::class.java)
            generateCode(dtoTrip)
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val formattedDate = dateFormat.format(dtoTrip.date)
            binding.tvDataDate.text = formattedDate
            binding.tvDataDepart.text = dtoTrip.cityStartingPoint
            binding.tvDataDestination.text = dtoTrip.cityDestination

            binding.tvToday.text = getCurrentDate()


        } catch (e: JsonSyntaxException) {
            binding.tvDataDate.text = "Erreur de parsing JSON"
            e.printStackTrace()
        }
    }


    fun getCurrentDate():String{
        val sdf = SimpleDateFormat("dd/MM/yyyy - HH:mm")
        return sdf.format(Date())
    }

    companion object {
        fun newInstance(dataQRcodeDriver: DtoInputTrip): GenerateQRCodeFragment {
            val fragment = GenerateQRCodeFragment()
            val gson = Gson()
            val json = gson.toJson(dataQRcodeDriver)
            val args = Bundle()
            args.putString("dataQRcodeDriver", json)
            fragment.arguments = args
            return fragment
        }
    }

    private fun setUpListeners() {
        binding.btnCode.setOnClickListener {
            val etCode: String = binding.etCode.text.toString()
            Log.e("etCode", etCode)
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
        val builder: StringBuilder = StringBuilder()

        builder.append(dto.plateNumber[0])
            .append(dto.cityStartingPoint[2])
            .append(dto.cityDestination[1])
            .append(dto.model[0])
            .append(dto.brand[0])

        code = builder.toString()
        Log.e("code", code)
    }
}