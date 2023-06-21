package com.example.yoel_beta.fragments

import android.app.Application
import androidx.annotation.NonNull
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.yoel_beta.models.FirebaseModel
import com.google.firebase.auth.FirebaseUser

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FirebaseModel = FirebaseModel(application)
    val userData: MutableLiveData<FirebaseUser> = repository.getFirebaseUserMutableLiveData()
    val loggedStatus: MutableLiveData<Boolean> = repository.getUserLoggedMutableLiveData()

    fun register(email: String, pass: String) {
        repository.register(email, pass)
    }

    fun signIn(email: String, pass: String) {
        repository.login(email, pass)
    }

    fun signOut() {
        repository.signOut()
    }

}
