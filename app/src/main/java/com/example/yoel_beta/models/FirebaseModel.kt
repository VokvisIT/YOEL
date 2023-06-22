package com.example.yoel_beta.models

import android.app.Application
import android.net.Uri
import android.os.Handler
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.util.Calendar
import java.util.Date

class FirebaseModel(private val application: Application) {
    private val firebaseUserMutableLiveData = MutableLiveData<FirebaseUser>()
    private val userLoggedMutableLiveData = MutableLiveData<Boolean>()
    private val userInfoMutableLiveData = MutableLiveData<Any>()
    private val taskModelListMutableLiveData = MutableLiveData<List<TaskModel>>()
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
    fun getTaskListMutableLiveData(): MutableLiveData<List<TaskModel>> {

        val uid = auth.currentUser?.uid.toString()
        val userTasksRef = tasks.child(uid)
        val taskModelListMutableLiveData = MutableLiveData<List<TaskModel>>()

        userTasksRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val taskModelList = mutableListOf<TaskModel>()

                for (taskSnapshot in snapshot.children) {
                    val task = taskSnapshot.getValue(TaskModel::class.java)
                    task?.let {
                        taskModelList.add(it)
                    }
                }

                // Sort the taskModelList based on the statusTask value
                val sortedList = taskModelList.sortedByDescending { it.getStatusTask() }

                taskModelListMutableLiveData.value = sortedList
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        return taskModelListMutableLiveData
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
            updateNowTimeForAllTasks()
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
        var imageUrl = ""
        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) { throw task.exception!! }
            photoRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                imageUrl = task.result.toString()
                register(email, pass, username, imageUrl)
            } else {
            }
        }
    }
    fun addTask(title:String, exp_task:Int, break_time: Float, status_task: Boolean){
        val taskModel = TaskModel(
            taskId = "",
            title = title,
            exp_task = exp_task,
            dataComp = 0,
            break_time = break_time,
            statusTask = status_task,
            nowTime = 0L
        )
        val newTaskRef = tasks.child(auth.currentUser!!.uid).push() // Создание нового узла для каждой задачи
        val taskId = newTaskRef.key // Получение уникального идентификатора задачи
        taskModel.setTaskId(taskId.toString()) // Присваиваем уникальный идентификатор задачи в модели задачи
        newTaskRef.setValue(taskModel).addOnCompleteListener { taskSnapshot ->
                if (taskSnapshot.isSuccessful) { } else { }
            }
    }
    fun removeTask(taskId: String){
        val taskRef = tasks.child(auth.currentUser!!.uid).child(taskId)
        taskRef.removeValue()
            .addOnCompleteListener { taskSnapshot ->
                if (taskSnapshot.isSuccessful) { } else { }
            }
    }
    fun setStatusTask(taskId: String, taskExp: Int){
        val taskRef = tasks.child(auth.currentUser!!.uid).child(taskId)
        val calendar: Calendar = Calendar.getInstance()
        val dataC: Date = calendar.time
        taskRef.child("statusTask").setValue(false)
            .addOnCompleteListener { taskSnapshot ->
                if (taskSnapshot.isSuccessful) {
                    taskRef.child("dataComp").setValue(ServerValue.TIMESTAMP)
                        .addOnCompleteListener { taskSnapshot ->
                            if (taskSnapshot.isSuccessful) {} else {}
                        }
                } else {}
            }
        setUserExp(taskExp)
    }
    fun setUserExp(exp: Int) {
        val uid = auth.currentUser?.uid
        val userRef = db.getReference("Users/$uid")
        userRef.child("exp").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val currentExp = dataSnapshot.getValue(Int::class.java) ?: 0
                val newExp = currentExp + exp
                userRef.child("exp").setValue(newExp)
                    .addOnCompleteListener { taskSnapshot ->
                        if (taskSnapshot.isSuccessful) {} else {}
                    }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(application, databaseError.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
    fun updateNowTimeForAllTasks() {
        val uid = auth.currentUser?.uid.toString()
        val userTasksRef = tasks.child(uid)

        userTasksRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (taskSnapshot in snapshot.children) {
                    val taskId = taskSnapshot.key.toString()
                    val taskRef = userTasksRef.child(taskId)
                    taskRef.child("nowTime").setValue(ServerValue.TIMESTAMP)
                        .addOnCompleteListener { taskSnapshot ->
                            if (taskSnapshot.isSuccessful) {
                                // Обработка успешного обновления nowTime
                            } else {
                                // Обработка ошибки при обновлении nowTime
                            }
                        }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Обработка ошибки чтения задач из базы данных
            }
        })
    }

}
