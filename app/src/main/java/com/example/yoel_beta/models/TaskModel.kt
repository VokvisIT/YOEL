package com.example.yoel_beta.models


data class TaskModel @JvmOverloads constructor(
    private var taskId: String,
    private var title: String,
    private var exp_task: Int,
    private var dataComp: Long,
    private var break_time: Float?,
    private var statusTask: Boolean
) {
    constructor() : this("","", 0, 0L, null, true)
    // Геттеры
    fun getTaskId(): String {
        return taskId
    }
    fun getTitle(): String {
        return title
    }

    fun getExpTask(): Int {
        return exp_task
    }

    fun getDataComp(): Long {
        return dataComp
    }

    fun getBreakTime(): Float? {
        return break_time
    }

    fun getStatusTask(): Boolean {
        return statusTask
    }

    // Сеттеры
    fun setTaskId (taskId: String){
        this.taskId = taskId
    }
    fun setTitle(title: String) {
        this.title = title
    }

    fun setExpTask(expTask: Int) {
        exp_task = expTask
    }

    fun setDataComp(dataComp: Long) {
        this.dataComp = dataComp
    }

    fun setBreakTime(breakTime: Float?) {
        break_time = breakTime
    }

    fun setStatusTask(statusTask: Boolean) {
        this.statusTask = statusTask
    }
}