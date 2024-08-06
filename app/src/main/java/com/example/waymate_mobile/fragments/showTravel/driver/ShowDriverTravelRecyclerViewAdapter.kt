package com.example.waymate_mobile.fragments.showTravel.driver

import android.content.Context
import android.content.SharedPreferences
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
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
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentShowDriverTravelItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val day: TextView = binding.itemDay
        val departure: TextView = binding.itemDeparture
        val destination: TextView = binding.itemDestination
    }
}