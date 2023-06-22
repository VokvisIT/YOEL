package com.example.yoel_beta.models

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.yoel_beta.R
import com.example.yoel_beta.fragments.AuthViewModel

class TaskAdapter(private val taskModels: MutableLiveData<List<TaskModel>>,
                  private val authViewModel: AuthViewModel) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return TaskViewHolder(view)
    }
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskModels.value?.get(position)
        // Настройте отображение данных задачи в элементе списка
        holder.bind(task!!)
    }
    override fun getItemCount(): Int {
        return taskModels.value?.size!!
    }
    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Определите элементы пользовательского интерфейса внутри элемента списка
        private val titleTextView: TextView = itemView.findViewById(R.id.task_title)
        private val expTitleView: TextView = itemView.findViewById(R.id.exp_task)
        private val checkboxflag: CheckBox = itemView.findViewById(R.id.round_checkbox)
        private val deltaskbtn: ImageView = itemView.findViewById(R.id.delTask)

        fun bind(taskModel: TaskModel) {
            // Привяжите данные задачи к элементам пользовательского интерфейса
            titleTextView.text = taskModel.getTitle().toString()
            expTitleView.text = taskModel.getExpTask().toString()
            checkboxflag.setOnClickListener{
                if (checkboxflag.isChecked){
                    authViewModel.setStatusTask(taskModel.getTaskId(), taskModel.getExpTask())
                }
            }
            deltaskbtn.setOnClickListener {
                authViewModel.delTask(taskModel.getTaskId())
            }
        }
    }
}
