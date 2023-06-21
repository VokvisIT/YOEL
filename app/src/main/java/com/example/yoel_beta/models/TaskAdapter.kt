//package com.example.yoel_beta.models
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.example.yoel_beta.R
//
//class TaskAdapter(private val tasks: List<Task>) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
//        return TaskViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
//        val task = tasks[position]
//        // Настройте отображение данных задачи в элементе списка
//        holder.bind(task)
//    }
//
//    override fun getItemCount(): Int {
//        return tasks.size
//    }
//
//    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        // Определите элементы пользовательского интерфейса внутри элемента списка
//        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
//
//        fun bind(task: Task) {
//            // Привяжите данные задачи к элементам пользовательского интерфейса
//            titleTextView.text = task.title
//        }
//    }
//}
