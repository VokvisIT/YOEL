package com.example.yoel_beta.ui.home

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
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
    private lateinit var viewmodel: AuthViewModel
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private lateinit var users: DatabaseReference
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()


        users.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)
                binding.homeUsername.text = user?.getUsername().toString() + " " + user?.getExp().toString()
                val srcurl = user?.getImageurl().toString()
                Glide.with(this@HomeFragment)
                    .load(srcurl)
                    .into(binding.imageUsername)
                Toast.makeText(
                    context,
                    "Photo Successfully $srcurl",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Обработка ошибок чтения данных
            }
        })

    }

    private fun init() {
        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        val uid:String = auth.currentUser?.uid.toString()
        users = db.getReference("Users/$uid")
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}