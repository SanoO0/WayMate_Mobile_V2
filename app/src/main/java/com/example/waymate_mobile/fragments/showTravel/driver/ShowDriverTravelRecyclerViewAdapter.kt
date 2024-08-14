package com.example.waymate_mobile.fragments.showTravel.driver

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import com.example.waymate_mobile.activities.MainActivity
import com.example.waymate_mobile.databinding.FragmentShowDriverTravelItemBinding
import com.example.waymate_mobile.dtos.trip.DtoInputTrip
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class ShowDriverTravelRecyclerViewAdapter(
    private val context: Context,
    private val values: List<DtoInputTrip>
) : RecyclerView.Adapter<ShowDriverTravelRecyclerViewAdapter.ViewHolder>() {
    lateinit var jwtToken: String
    lateinit var prefs: SharedPreferences
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        prefs = context.getSharedPreferences("waymate", Context.MODE_PRIVATE)
        jwtToken = prefs.getString("jwtToken", "") ?: ""
        return ViewHolder(
            FragmentShowDriverTravelItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        CoroutineScope(Dispatchers.Main).launch {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val formattedDate = dateFormat.format(item.date)
            holder.day.text = formattedDate
            holder.departure.text = item.cityStartingPoint
            holder.destination.text = item.cityDestination
            holder.generateQRCodeButton.setOnClickListener {
                (holder.itemView.context as MainActivity).showGenerateQRCode(item)
            }
            holder.basicDetails.setOnLongClickListener {
                val context = holder.itemView.context
                if (context is MainActivity) {
                    if(holder.showDetails) {
                        holder.details.visibility = View.GONE
                        holder.showDetails = false
                        Log.d("LongPress", "Done Gone")
                    } else {
                        holder.details.visibility = View.VISIBLE

                        val format = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                        val date = format.format(item.date)

                        holder.dayDetails.text = date
                        holder.cityDetails.text = "${item.cityStartingPoint} --> ${item.cityDestination}"
                        holder.priceDetails.text = item.price.toString()

                        holder.showDetails = true
                        Log.d("LongPress", "Done Visible")
                    }
                }
                true
            }
            holder.btnModify.setOnClickListener {
                (holder.itemView.context as MainActivity).showModifyTrip(item)
            }
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentShowDriverTravelItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val day: TextView = binding.itemDay
        val departure: TextView = binding.itemDeparture
        val destination: TextView = binding.itemDestination
        val generateQRCodeButton: ImageButton = binding.btnQRcode
        var showDetails: Boolean = false
        val details: LinearLayout = binding.showDetailsLayout
        val basicDetails: LinearLayout = binding.basicDetailsLayout
        val dayDetails: TextView = binding.etDayDetails
        val cityDetails: TextView = binding.etCityDetails
        val priceDetails: TextView = binding.etPriceDetails
        val btnModify: Button = binding.btnModify
    }
}