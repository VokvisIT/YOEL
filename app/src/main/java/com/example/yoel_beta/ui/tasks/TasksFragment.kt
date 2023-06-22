package com.example.yoel_beta.ui.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.yoel_beta.R
import com.example.yoel_beta.databinding.FragmentTasksBinding
import com.example.yoel_beta.fragments.AuthViewModel

class TasksFragment : Fragment() {
    private lateinit var viewModel: AuthViewModel
    private lateinit var binding: FragmentTasksBinding
    private lateinit var navControl: NavController
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
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)).get(AuthViewModel::class.java
        )
        navControl = Navigation.findNavController(view)
        binding.btnCreateTask.setOnClickListener(){
            val title = binding.taskName.text.toString().trim()
            val exp_task:Int = binding.sliderExpValue.value.toInt()
            val status_task = true
            val break_time: Float = binding.sliderTimeValue.value
            if (title.isNotEmpty()) {
                viewModel.addTask(title=title,exp_task=exp_task, status_task=status_task,break_time=break_time)
                navControl.navigate(R.id.action_navigation_tasks_to_navigation_home)
            }
        }
    }
}

