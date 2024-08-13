package com.example.waymate_mobile.fragments.account

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.waymate_mobile.databinding.FragmentPassengerAccountBinding
import com.example.waymate_mobile.dtos.user.DtoInputUser
import com.example.waymate_mobile.repositories.IUserRepository
import com.example.waymate_mobile.utils.RetrofitFactory
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PassengerAccountFragment : Fragment() {
    private lateinit var binding: FragmentPassengerAccountBinding
    private lateinit var userRepository: IUserRepository
    private lateinit var user: DtoInputUser

    // Inflates the fragment's layout and initializes binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPassengerAccountBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    // Called after the fragment's view has been created, suitable for UI initialization
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Launch a coroutine to fetch user data
        lifecycleScope.launch {
            getUserData()
        }
    }

    // Fetches user data from the repository
    private suspend fun getUserData() {
        val prefs = requireActivity().getSharedPreferences("waymate", Context.MODE_PRIVATE)
        val token = prefs.getString("jwtToken", "") ?: ""
        userRepository = RetrofitFactory.create(token, IUserRepository::class.java)

        try {
            val response = userRepository.getUserData()
            if(response != null) {
                user = response
                setUpDriverData()
            }
        } catch (e: Exception) {
            Log.e("Error", e.message.toString())
        }
    }

    // Populates the UI with the user data retrieved from the server
    private fun setUpDriverData(){
        // Set user name
        binding.userName.text = buildString {
            append("Name : ")
            append(user.firstName)
            append(" ")
            append(user.lastName)
        }
        // Set username
        binding.userUsername.text = buildString {
            append("Username : ")
            append(user.username)
        }
        // Set email
        binding.userMail.text = buildString {
            append("Mail : ")
            append(user.email)
        }
        // Format and set birthdate
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(user.birthdate)
        binding.userBirthdate.text = buildString {
            append("Birthdate : ")
            append(formattedDate)
        }
        // Set phone number
        binding.userPhone.text = buildString {
            append("Phone number : ")
            append(user.phoneNumber)
        }
        // Set gender
        binding.userGender.text = buildString {
            append("Gender : ")
            append(user.gender)
        }
        // Set city
        binding.userCity.text = buildString {
            append("City : ")
            append(user.city)
        }
    }

    // Companion object to create a new instance of the fragment
    companion object {
        @JvmStatic
        fun newInstance() = PassengerAccountFragment()
    }
}