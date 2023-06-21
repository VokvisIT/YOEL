package com.example.yoel_beta.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.yoel_beta.HomeActivity
import com.example.yoel_beta.R
import com.example.yoel_beta.databinding.FragmentSignInBinding


class SignInFragment : Fragment() {

    private lateinit var binding: FragmentSignInBinding
    private lateinit var navControl: NavController
    private lateinit var viewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navControl= Navigation.findNavController(view)
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)).get(AuthViewModel::class.java
        )
        viewModel.userData.observe(viewLifecycleOwner, Observer { firebaseUser ->
            if (firebaseUser != null) {
                Toast.makeText(context, "Login Successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(activity, HomeActivity::class.java)
                startActivity(intent)
            }
        })
        binding.createAccountTextView.setOnClickListener {
            navControl.navigate(R.id.action_signInFragment_to_signUpFragment)
        }
        binding.logBtn.setOnClickListener{
            val email = binding.emailEt.text.toString().trim()
            val pass = binding.passEt.text.toString().trim()
            if (email.isNotEmpty() && pass.isNotEmpty()){
                viewModel.signIn(email, pass)
            }
        }
    }
}