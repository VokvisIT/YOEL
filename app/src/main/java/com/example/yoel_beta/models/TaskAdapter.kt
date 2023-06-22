package com.example.yoel_beta.models

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.yoel_beta.R
import com.example.yoel_beta.fragments.AuthViewModel
import com.google.android.material.card.MaterialCardView
import java.text.DecimalFormat
import kotlin.math.roundToInt

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
        private val card: MaterialCardView = itemView.findViewById(R.id.card_rv)
        private val timebreack: TextView = itemView.findViewById(R.id.break_time)
        private val progressBar: ProgressBar = itemView.findViewById(R.id.progressbar)
        fun bind(taskModel: TaskModel) {
            // Привяжите данные задачи к элементам пользовательского интерфейса
            if(taskModel.getStatusTask() == false){
                card.alpha = 0.5F
                card.isClickable = false
                checkboxflag.isChecked = true
                checkboxflag.isClickable=false
                var time1 = (taskModel.getBreakTime()!! -((taskModel.getNowTime().toFloat() - taskModel.getDataComp().toFloat())/3600000.0))
                val decimalFormat = DecimalFormat("#.#")
                timebreack.text = decimalFormat.format(time1)
                progressBar.progress = (((time1-taskModel.getBreakTime()!!)/taskModel.getBreakTime()!!)*100).toInt()
            }else{
                timebreack.text = taskModel.getBreakTime().toString()
                checkboxflag.setOnClickListener{
                    if (checkboxflag.isChecked){
                        authViewModel.setStatusTask(taskModel.getTaskId(), taskModel.getExpTask())
                    }
                }

            }
            deltaskbtn.setOnClickListener {
                authViewModel.delTask(taskModel.getTaskId())
            }
            titleTextView.text = taskModel.getTitle().toString()
            expTitleView.text = taskModel.getExpTask().toString()

        }
    }
}
