package com.example.yoel_beta.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.yoel_beta.HomeActivity
import com.example.yoel_beta.R


class SplashFragment : Fragment() {
    private lateinit var navController: NavController
    private lateinit var viewModel: AuthViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)).get(AuthViewModel::class.java
        )
        Handler(Looper.myLooper()!!).postDelayed({
            if (viewModel.userData.value != null){
                val intent = Intent(activity, HomeActivity::class.java)
                startActivity(intent)
            }else{
                navController.navigate(R.id.action_splashFragment_to_signInFragment)
            }
        }, 2000)
    }

    private fun init(view: View) {
        navController = Navigation.findNavController(view)
    }
}