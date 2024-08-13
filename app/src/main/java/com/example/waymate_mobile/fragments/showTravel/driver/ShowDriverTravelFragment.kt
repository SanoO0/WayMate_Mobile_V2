package com.example.waymate_mobile.fragments.showTravel.driver

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.waymate_mobile.R
import com.example.waymate_mobile.dtos.trip.DtoInputTrip

class ShowDriverTravelFragment : Fragment() {
    private val tripUI: ArrayList<DtoInputTrip> = arrayListOf()
    lateinit var showDriverTravelRecyclerViewAdapter: ShowDriverTravelRecyclerViewAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_show_driver_travel_list, container, false)
        if (view is RecyclerView) {
            showDriverTravelRecyclerViewAdapter =
                ShowDriverTravelRecyclerViewAdapter(requireContext(), tripUI)
            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            view.layoutManager = layoutManager
            view.adapter = showDriverTravelRecyclerViewAdapter
        }
        return view
    }

    fun initUIWithTrip(trips: List<DtoInputTrip>?) {
        trips?.let {
            tripUI.clear()
            tripUI.addAll(it)
            showDriverTravelRecyclerViewAdapter.notifyDataSetChanged()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ShowDriverTravelFragment()
    }
}
