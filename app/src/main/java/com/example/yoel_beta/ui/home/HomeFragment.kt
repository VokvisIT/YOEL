package com.example.yoel_beta.ui.home

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

class HomeFragment : Fragment() {
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
                Log.d("TAG1", "///////////////////////////////")
                binding.homeUsername.text = user?.getUsername().toString()
                binding.imageUsername.setImageURI(Uri.parse(user?.getImageUrl().toString()))
                Log.d("TAG2", "///////////////////////////////")
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