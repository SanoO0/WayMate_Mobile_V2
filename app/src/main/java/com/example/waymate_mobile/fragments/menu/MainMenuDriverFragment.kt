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
        binding.btnShowMyTravel.setOnClickListener {
            (requireActivity() as MainActivity).showDriverTravel()
        }
        binding.btnDisconnection.setOnClickListener {
            (requireActivity() as MainActivity).signOut()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainMenuDriverFragment()
    }
}