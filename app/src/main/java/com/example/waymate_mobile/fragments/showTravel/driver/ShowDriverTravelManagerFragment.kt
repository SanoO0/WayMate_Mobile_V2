package com.example.waymate_mobile.fragments.showTravel.driver

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.waymate_mobile.R
import com.example.waymate_mobile.databinding.FragmentShowDriverTravelManagerBinding
import kotlinx.coroutines.launch

class ShowDriverTravelManagerFragment : Fragment() {
    private lateinit var binding: FragmentShowDriverTravelManagerBinding
    private lateinit var viewModel: ShowDriverTravelManagerViewModel
    companion object {
        fun newInstance(): ShowDriverTravelManagerFragment {
            val fragment = ShowDriverTravelManagerFragment()
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShowDriverTravelManagerBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val prefs = requireActivity().getSharedPreferences("waymate", Context.MODE_PRIVATE)
        val token = prefs.getString("jwtToken", "") ?: ""

        viewModel = ShowDriverTravelManagerViewModel(token)

        val showTravelFragment = childFragmentManager.findFragmentById(R.id.fcv_showDriverTravel_list) as ShowDriverTravelFragment

        viewModel.mutableTripLivaData.observe(viewLifecycleOwner) {
            showTravelFragment.initUIWithTrip(it)
        }
        lifecycleScope.launch {
            viewModel.startGetAllTrips()
        }

        viewModel.mutableCount.observe(viewLifecycleOwner) {
            if(it == -1) {
                binding.btnLoadMoreTrips.visibility = View.INVISIBLE
                binding.btnLoadMoreTrips.isEnabled = false
            } else if (it % 5 == 0) {
                binding.btnLoadMoreTrips.visibility = View.VISIBLE
                binding.btnLoadMoreTrips.isEnabled = true
            } else {
                binding.btnLoadMoreTrips.visibility = View.INVISIBLE
                binding.btnLoadMoreTrips.isEnabled = false
            }
        }

        binding.btnLoadMoreTrips.setOnClickListener {
            viewModel.showCount += 5
            lifecycleScope.launch {
                viewModel.startGetAllTrips()
            }
        }
    }
}