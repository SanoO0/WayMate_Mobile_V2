package com.example.waymate_mobile.fragments.showTravel.passenger

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.waymate_mobile.R
import com.example.waymate_mobile.dtos.trip.DtoInputTrip

class ShowPassengerTravelFragment : Fragment() {
    private val tripUI: ArrayList<DtoInputTrip> = arrayListOf()
    lateinit var showPassengerTravelRecyclerViewAdapter: ShowPassengerTraveRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_show_passenger_travel_list, container, false)

        if (view is RecyclerView) {
            showPassengerTravelRecyclerViewAdapter =
                ShowPassengerTraveRecyclerViewAdapter(requireContext(), tripUI)
            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            view.layoutManager = layoutManager
            view.adapter = showPassengerTravelRecyclerViewAdapter
        }
        return view
    }

    fun initUIWithTrip(trips: List<DtoInputTrip>?) {
        trips?.let {
            tripUI.clear()
            tripUI.addAll(it)
            showPassengerTravelRecyclerViewAdapter.notifyDataSetChanged()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ShowPassengerTravelFragment()
    }
}