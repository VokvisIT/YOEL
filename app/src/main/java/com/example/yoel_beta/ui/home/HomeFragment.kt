package com.example.yoel_beta.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yoel_beta.databinding.FragmentHomeBinding
import com.bumptech.glide.Glide
import com.example.yoel_beta.fragments.AuthViewModel
import com.example.yoel_beta.models.TaskAdapter

class HomeFragment : Fragment() {
    private lateinit var viewModel: AuthViewModel
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)).get(AuthViewModel::class.java
        )
        viewModel.userinfo.observe(viewLifecycleOwner, Observer { userInfo ->
            if (userInfo is Map<*, *>) { // Проверяем, что userInfo является Map
                val name = userInfo["name"] as String
                val imageUrl = userInfo["imageurl"] as String
                val expLevel = userInfo["explvl"] as Int
                val expCurrent = userInfo["expcurrent"] as Int
                val exp = userInfo["exp"] as Int
                binding.homeUsername.text = name
                Glide.with(this@HomeFragment)
                    .load(imageUrl)
                    .into(binding.imageUsername)
                var result: Double = (exp.toDouble() / expCurrent)*100
                binding.circularProgressIndicator.setProgressCompat(result.toInt(), true)
                binding.exp.text = exp.toString()
                binding.expcurrent.text = expCurrent.toString()
                binding.lvl.text = expLevel.toString()
            }
        })
        viewModel.taskList.observe(viewLifecycleOwner, Observer {
            if (::viewModel.isInitialized) {
                binding.tasksRv.layoutManager = LinearLayoutManager(context)
                val taskModels = viewModel.taskList
                val taskAdapter = TaskAdapter(taskModels, viewModel)
                binding.tasksRv.adapter = taskAdapter
            }
        })
    }
}