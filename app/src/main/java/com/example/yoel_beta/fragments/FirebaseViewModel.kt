package com.example.yoel_beta.fragments

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.yoel_beta.models.FirebaseModel
import com.google.firebase.auth.FirebaseUser

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FirebaseModel = FirebaseModel(application)
    val userData: MutableLiveData<FirebaseUser> = repository.getFirebaseUserMutableLiveData()
    val loggedStatus: MutableLiveData<Boolean> = repository.getUserLoggedMutableLiveData()
    fun signIn(email: String, pass: String) {
        repository.login(email, pass)
    }

    fun signOut() {
        repository.signOut()
    }
    fun register(email: String, pass: String,photoUri: Uri, username: String){
        return repository.reguser(url = photoUri, username = username, email = email,pass = pass )
    }
}
