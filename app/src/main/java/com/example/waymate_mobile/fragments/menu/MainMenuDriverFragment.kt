package com.example.waymate_mobile.fragments.menu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.waymate_mobile.activities.MainActivity
import com.example.waymate_mobile.databinding.FragmentMainMenuDriverBinding

class MainMenuDriverFragment : Fragment() {
    private lateinit var binding: FragmentMainMenuDriverBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainMenuDriverBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mainActivity = requireActivity() as MainActivity
        binding.btnShowMyTravel.setOnClickListener {
            mainActivity.showDriverTravel()
        }
        binding.btnViewProfile.setOnClickListener {
            mainActivity.showProfileDriver()
        }
        binding.btnAddNewTravel.setOnClickListener {
            mainActivity.showAddNewTrip()
        }
        binding.btnDisconnection.setOnClickListener {
            mainActivity.signOut()
        }
        mainActivity.changeTopMenu(0, false, "")
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainMenuDriverFragment()
    }
}