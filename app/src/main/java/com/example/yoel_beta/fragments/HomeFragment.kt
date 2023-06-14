package com.example.yoel_beta.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.yoel_beta.R
import com.example.yoel_beta.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var navControl: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
        logout()
    }
    private fun init(view: View){
        navControl= Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
    }
    private fun logout(){
        binding.logOut.setOnClickListener {
            auth.signOut()
            navControl.navigate(R.id.action_homeFragment_to_signInFragment)
        }
    }
}