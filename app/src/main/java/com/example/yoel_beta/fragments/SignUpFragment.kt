package com.example.yoel_beta.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.yoel_beta.HomeActivity
import com.example.yoel_beta.R
import com.example.yoel_beta.databinding.FragmentSignUpBinding
import com.example.yoel_beta.models.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private lateinit var users: DatabaseReference
    private lateinit var navControl: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
        registerEvents()
    }
    private fun init(view: View){
        navControl= Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        users = db.getReference("Users")
    }
    private fun registerEvents(){
        binding.authTextView.setOnClickListener {
            navControl.navigate(R.id.action_signUpFragment_to_signInFragment2)
        }
        binding.logBtn.setOnClickListener{
            val email = binding.emailEt.text.toString().trim()
            val pass = binding.passEt.text.toString().trim()
            val veriyPass = binding.verifyPassEt.text.toString().trim()

            if (email.isNotEmpty() && pass.isNotEmpty() && veriyPass.isNotEmpty()){
                if (pass == veriyPass){
                    auth.createUserWithEmailAndPassword(email, pass).addOnSuccessListener(
                        OnSuccessListener {
                            var user = User(email, pass, 0)
                            users.child(auth.currentUser!!.uid)
                                .setValue(user)
                                .addOnCompleteListener(
                                    OnCompleteListener {
                                        if (it.isSuccessful){
                                            Toast.makeText(context, "Registered Successfully", Toast.LENGTH_SHORT).show()
                                            val intent = Intent(activity, HomeActivity::class.java)
                                            startActivity(intent)
                                        }else{
                                            Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                )
                        }
                    )
                }
            }
        }
    }
}