package com.example.waymate_mobile.fragments.account

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.waymate_mobile.R
import com.example.waymate_mobile.databinding.FragmentDriverAccountBinding
import com.example.waymate_mobile.dtos.users.user.DtoInputUser
import com.example.waymate_mobile.repositories.users.user.IUserRepository
import com.example.waymate_mobile.utils.RetrofitFactory
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class DriverAccountFragment : Fragment() {
    private lateinit var binding: FragmentDriverAccountBinding
    private lateinit var userRepository: IUserRepository
    private lateinit var user: DtoInputUser
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDriverAccountBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            getUserData()
        }
    }

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
            Log.e("Echec", e.message.toString())
        }
    }

    private fun setUpDriverData(){
        binding.userName.text = buildString {
            append("Name : ")
            append(user.firstName)
            append(" ")
            append(user.lastName)
        }
        binding.userName.text = buildString {
            append("Username : ")
            append(user.username)
        }
        binding.userMail.text = buildString {
            append("Mail : ")
            append(user.email)
        }

        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(user.birthdate)

        binding.userBirthdate.text = buildString {
            append("Birthdate : ")
            append(formattedDate)
        }
        binding.userPhone.text = buildString {
            append("Phone number : ")
            append(user.phoneNumber)
        }
        binding.userGender.text = buildString {
            append("Gender : ")
            append(user.gender)
        }
        binding.userCity.text = buildString {
            append("City : ")
            append(user.city)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = DriverAccountFragment()
    }
}