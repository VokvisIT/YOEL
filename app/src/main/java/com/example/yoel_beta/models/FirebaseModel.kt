package com.example.yoel_beta.models
import android.app.Application
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlin.collections.List as CollectionsList

class FirebaseModel(private val application: Application) {
    private val firebaseUserMutableLiveData = MutableLiveData<FirebaseUser>()
    private val userLoggedMutableLiveData = MutableLiveData<Boolean>()
    private val userInfoMutableLiveData = MutableLiveData<Any>()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var db: FirebaseDatabase
    private val users: DatabaseReference
    val storageRef = FirebaseStorage.getInstance().reference
    private var tasks: DatabaseReference

    fun getFirebaseUserMutableLiveData(): MutableLiveData<FirebaseUser> {
        return firebaseUserMutableLiveData
    }

    fun getUserLoggedMutableLiveData(): MutableLiveData<Boolean> {
        return userLoggedMutableLiveData
    }
    fun getUserInfoMutableLiveData():MutableLiveData<Any>{
        val uid:String = auth.currentUser?.uid.toString()
        var users = db.getReference("Users/$uid")
        users.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)
                var exp = user?.getExp()!!.toInt()
                var (a, b, c) = calculateLevelInfo(exp!!.toInt())
                val dictionary = mapOf(
                    "name" to user?.getUsername().toString(),
                    "imageurl" to user?.getImageurl().toString(),
                    "explvl" to a,
                    "expcurrent" to b,
                    "exp" to c
                )
                userInfoMutableLiveData.value = dictionary
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(application, databaseError?.message, Toast.LENGTH_SHORT).show()
            }
        })
        return userInfoMutableLiveData
    }
    init {
        firebaseUserMutableLiveData.value = auth.currentUser
        db = FirebaseDatabase.getInstance()
        users = db.getReference("Users")
        tasks = db.getReference("Tasks")
        if (auth.currentUser != null) {
            firebaseUserMutableLiveData.postValue(auth.currentUser)
        }
    }

    fun register(email: String, pass: String, username: String, imageUrl: String) {
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
                        .addOnCompleteListener { task: Task<Void> ->
                            if (task.isSuccessful) {
                                firebaseUserMutableLiveData.postValue(auth.currentUser)
                            } else {
                                Toast.makeText(application, task.exception?.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            )
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
    fun reguser(url: Uri, username: String, email: String, pass: String) {
        val photoRef = storageRef.child("photos/$username-image.jpeg")
        val uploadTask = photoRef.putFile(url!!)
        var imageUrl: String = ""
        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) { throw task.exception!! }
            photoRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                imageUrl = task.result.toString()
                register(email, pass, username, imageUrl)
            } else {
                Toast.makeText(application, "Фото НЕ ЗАГРУЗИЛОСЬ", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun addTask(title:String, exp_task:Int, data_comp:Long, break_time: Float, status_task: Boolean){

        val task = Task(
            title = title,
            exp_task = exp_task,
            data_comp = data_comp,
            break_time = break_time,
            status_task = status_task,
        )
        tasks.child(auth.currentUser!!.uid)
            .push() // Создание нового узла для каждой задачи
            .setValue(task)
            .addOnCompleteListener { taskSnapshot ->
                if (taskSnapshot.isSuccessful) {
                    Toast.makeText(
                        application,
                        "Created Task: $title",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        application,
                        taskSnapshot.exception?.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
    fun getUserInfo(){

    }
}
