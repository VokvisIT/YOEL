package com.example.yoel_beta.ui.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.yoel_beta.R
import com.example.yoel_beta.databinding.FragmentTasksBinding
import com.example.yoel_beta.models.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.Calendar
import java.util.Date

class TasksFragment : Fragment() {

    private lateinit var binding: FragmentTasksBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private lateinit var navControl: NavController
    private lateinit var tasks: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTasksBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        CreateTaskEvents()
        init(view)
    }

    private fun init(view: View) {
        navControl = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        tasks = db.getReference("Tasks")
    }

    private fun CreateTaskEvents(){
        binding.btnCreateTask.setOnClickListener(){
            val title = binding.taskName.text.toString().trim()
            val exp_task:Int = binding.sliderExpValue.value.toInt()
            val status_task = true
            val calendar: Calendar = Calendar.getInstance()
            val data_comp: Date = calendar.time
            val break_time: Float = binding.sliderTimeValue.value
            Toast.makeText(
                context,
                "${data_comp.time}",
                Toast.LENGTH_SHORT
            ).show()
            if (title.isNotEmpty()) {
                val task = Task(
                    title = title,
                    exp_task = exp_task,
                    data_comp = data_comp.time,
                    break_time = break_time,
                    status_task = status_task,
                )
                tasks.child(auth.currentUser!!.uid)
                    .push() // Создание нового узла для каждой задачи
                    .setValue(task)
                    .addOnCompleteListener { taskSnapshot ->
                        if (taskSnapshot.isSuccessful) {
                            Toast.makeText(
                                context,
                                "Created Successfully $title",
                                Toast.LENGTH_SHORT
                            ).show()
                            navControl.navigate(R.id.action_navigation_tasks_to_navigation_home)
                        } else {
                            Toast.makeText(
                                context,
                                taskSnapshot.exception?.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
    }
}

