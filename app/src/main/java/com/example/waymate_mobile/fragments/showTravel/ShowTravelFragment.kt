package com.example.waymate_mobile.fragments.showTravel

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.waymate_mobile.R
import com.example.waymate_mobile.activities.MainActivity
import com.example.waymate_mobile.databinding.FragmentShowTravelBinding
import com.example.waymate_mobile.fragments.showTravel.driver.ShowDriverTravelManagerFragment
import com.example.waymate_mobile.fragments.showTravel.passenger.ShowPassengerTravelManagerFragment

class ShowTravelFragment : Fragment() {
    private lateinit var binding: FragmentShowTravelBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShowTravelBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if((requireActivity() as MainActivity).userType == "Driver") {
            val showTravelFragment = ShowDriverTravelManagerFragment.newInstance()
            replaceFragment(showTravelFragment)
        } else if ((requireActivity() as MainActivity).userType == "Passenger") {
            val showTravelFragment = ShowPassengerTravelManagerFragment.newInstance()
            replaceFragment(showTravelFragment)
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.fcv_fragmentShowTravel, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
    companion object {
        @JvmStatic
        fun newInstance() = ShowTravelFragment()
    }
}