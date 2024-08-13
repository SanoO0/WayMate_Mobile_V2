package com.example.waymate_mobile.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.waymate_mobile.databinding.ActivityQrscanBinding
import com.example.waymate_mobile.dtos.trip.DtoInputTrip
import com.google.gson.Gson
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions

class QRScanActivity() : AppCompatActivity() {
    private lateinit var binding: ActivityQrscanBinding
    private lateinit var dto: DtoInputTrip
    // Request permission launcher for camera access
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            isGranted: Boolean ->
            if(isGranted) {
                showCamera()
            }
            else {
                // Permission denied - explain to the user
                Toast.makeText(this, "Camera permission is required to scan QR codes.", Toast.LENGTH_SHORT).show()
            }
        }

    // Launcher for the QR code scanner
    private val scanLauncher =
        registerForActivityResult(ScanContract()) {result: ScanIntentResult ->
            run {
                if (result.contents == null) {
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
                } else {
                    setResult(result.contents)
                }
            }
        }

    // Handle the QR code scan result
    private fun setResult(contents: String) {
        val tripScan = Gson().fromJson(contents, DtoInputTrip::class.java)
        if(tripScan == dto) {
            binding.textView.text = "Scan Sucessfull"
            generateCode()
        } else {
            binding.textView.text = "Scan error, please try again"
        }
    }


    // Show the camera to scan QR code
    private fun showCamera() {
        val options = ScanOptions()
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
        options.setPrompt("Scan QR code")
        options.setCameraId(0)
        options.setBeepEnabled(false)
        options.setBarcodeImageEnabled(true)
        options.setOrientationLocked(false)

        scanLauncher.launch(options)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initViews()

        // Retrieve the trip data passed through the intent
        val json = intent.getStringExtra("TRIP_DATA_JSON")
        dto = Gson().fromJson(json, DtoInputTrip::class.java)
    }

    private fun initViews() {
        checkPermissionCamera(this)
    }

    // Check if the camera permission is granted, and if not, request it
    @SuppressLint("NewApi")
    private fun checkPermissionCamera(context: Context) {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            showCamera()
        }
        else if (shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)) {
            Toast.makeText(context, "CAMERA permission required", Toast.LENGTH_SHORT).show()
        }
        else {
            requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
        }
    }

    private fun initBinding() {
        binding = ActivityQrscanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnMainMenu.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        binding.btnScan.setOnClickListener {
            showCamera()
        }
    }

    // Generate a code based on the trip details
    private fun generateCode() {
        val code = StringBuilder().apply {
            append(dto.plateNumber[0])
            append(dto.cityStartingPoint[2])
            append(dto.cityDestination[1])
            append(dto.model[0])
            append(dto.brand[0])
        }.toString()

        binding.tvCode.text = code
    }
}