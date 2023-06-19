package com.example.yoel_beta.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
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
import com.google.firebase.storage.FirebaseStorage


class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private lateinit var users: DatabaseReference
    private lateinit var navControl: NavController
    private val GALLERY_REQUEST_CODE = 1
    var photoUri: Uri? = null
    val storageRef = FirebaseStorage.getInstance().reference

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
        openFileManager()
        registerEvents()
    }
    private fun openFileManager() {
        binding.imageloader.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, GALLERY_REQUEST_CODE)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            photoUri = data?.data
            binding.pathimageuri.text = photoUri.toString()

        }
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
            val username = binding.username.text.toString().trim()
            val email = binding.emailEt.text.toString().trim()
            val pass = binding.passEt.text.toString().trim()
            val veriyPass = binding.verifyPassEt.text.toString().trim()
            val photoRef = storageRef.child("photos/$username-image.jpeg")
            var imageUrl: String = ""
            if (photoUri != null) {
                val uploadTask = photoRef.putFile(photoUri!!)
                uploadTask.continueWithTask { task ->
                    if (!task.isSuccessful) {
                        throw task.exception!!
                    }
                    // Получение ссылки на загруженный файл
                    photoRef.downloadUrl
                }.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Получение ссылки на загруженный файл
                        imageUrl = task.result.toString()
                        Toast.makeText(context, "Фото: ${imageUrl.toString()}", Toast.LENGTH_SHORT)
                            .show()

                        // Здесь вы можете продолжить выполнение кода, который требует значения imageUrl, например, сохранение пользователя в базе данных
                        if (email.isNotEmpty() && pass.isNotEmpty() && veriyPass.isNotEmpty()) {
                            if (pass == veriyPass) {
                                auth.createUserWithEmailAndPassword(email, pass)
                                    .addOnSuccessListener(
                                        OnSuccessListener {
                                            var user = User(
                                                username = username,
                                                imageurl = imageUrl.toString(),
                                                email = email,
                                                pass = pass,
                                                exp = 0
                                            )
                                            users.child(auth.currentUser!!.uid)
                                                .setValue(user)
                                                .addOnCompleteListener(
                                                    OnCompleteListener {
                                                        if (it.isSuccessful) {
                                                            Toast.makeText(
                                                                context,
                                                                "Registered Successfully $username",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                            val intent = Intent(
                                                                activity,
                                                                HomeActivity::class.java
                                                            )
                                                            startActivity(intent)
                                                        } else {
                                                            Toast.makeText(
                                                                context,
                                                                it.exception?.message,
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                        }
                                                    }
                                                )
                                        }
                                    )
                            }
                        }
                    } else {
                        Toast.makeText(context, "Фото НЕ ЗАГРУЗИЛОСЬ", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}