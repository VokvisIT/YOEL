package com.example.yoel_beta.ui.home

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.yoel_beta.databinding.FragmentHomeBinding
import com.example.yoel_beta.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.bumptech.glide.Glide
import com.example.yoel_beta.HomeActivity
import com.example.yoel_beta.fragments.AuthViewModel

class HomeFragment : Fragment() {
    private lateinit var viewModel: AuthViewModel
    private lateinit var binding: FragmentHomeBinding




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)).get(AuthViewModel::class.java
        )
        viewModel.userinfo.observe(viewLifecycleOwner, Observer { userInfo ->
            if (userInfo is Map<*, *>) { // Проверяем, что userInfo является Map
                val name = userInfo["name"] as String
                val imageUrl = userInfo["imageurl"] as String
                val expLevel = userInfo["explvl"] as Int
                val expCurrent = userInfo["expcurrent"] as Int
                val exp = userInfo["exp"] as Int
                binding.homeUsername.text = name
                Glide.with(this@HomeFragment)
                    .load(imageUrl)
                    .into(binding.imageUsername)
                var result: Double = (exp.toDouble() / expCurrent)*100
                Toast.makeText(context, "$expLevel $expCurrent $exp $result", Toast.LENGTH_SHORT).show()
                binding.circularProgressIndicator.setProgressCompat(result.toInt(), true)
                binding.exp.text = exp.toString()
                binding.expcurrent.text = expCurrent.toString()
                binding.lvl.text = expLevel.toString()


            }
        })

    }
}