package com.example.waymate_mobile.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
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
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            isGranted: Boolean ->
            if(isGranted) {
                showCamera()
            }
            else {
               //explain
            }
        }
    
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

    private fun setResult(contents: String) {
        Toast.makeText(this, contents, Toast.LENGTH_SHORT).show()
        val gson = Gson()
        val tripScan = gson.fromJson(contents, DtoInputTrip::class.java)
        if(tripScan == dto) {
            binding.textView.setText("Scan Sucessfull")
        } else {
            binding.textView.setText("Error scan please retry")
        }
    }


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

        val json = intent.getStringExtra("TRIP_DATA_JSON")
        val gson = Gson()
        dto = gson.fromJson(json, DtoInputTrip::class.java)
    }

    private fun initViews() {
        checkPermissionCamera(this)
    }

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
}