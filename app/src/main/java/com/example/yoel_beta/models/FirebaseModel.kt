package com.example.yoel_beta.models
import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class FirebaseModel(private val application: Application) {
    private val firebaseUserMutableLiveData = MutableLiveData<FirebaseUser>()
    private val userLoggedMutableLiveData = MutableLiveData<Boolean>()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun getFirebaseUserMutableLiveData(): MutableLiveData<FirebaseUser> {
        return firebaseUserMutableLiveData
    }

    fun getUserLoggedMutableLiveData(): MutableLiveData<Boolean> {
        return userLoggedMutableLiveData
    }

    init {
        firebaseUserMutableLiveData.value = auth.currentUser

        if (auth.currentUser != null) {
            firebaseUserMutableLiveData.postValue(auth.currentUser)
        }
    }

    fun register(email: String, pass: String) {
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task: Task<AuthResult> ->
                if (task.isSuccessful) {
                    firebaseUserMutableLiveData.postValue(auth.currentUser)
                } else {
                    Toast.makeText(application, task.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun login(email: String, pass: String) {
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task: Task<AuthResult> ->
                if (task.isSuccessful) {
                    firebaseUserMutableLiveData.postValue(auth.currentUser)
                } else {
                    Toast.makeText(application, task.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun signOut() {
        auth.signOut()
        userLoggedMutableLiveData.postValue(true)
    }
}
