package com.example.yoel_beta.models

import java.util.Date

data class Task @JvmOverloads constructor(
    private var title: String = "",
    private var exp_task: Int = 0,
    private var data_comp: Long,
    private var break_time: Float? = null,
    private var status_task: Boolean = true,
) {
    // Конструктор
    init {
        this.title = title
        this.exp_task = exp_task
        this.data_comp = data_comp
        this.break_time = break_time
        this.status_task = status_task
    }
    // Геттеры
    fun getTitle(): String {
        return title
    }

    fun getExpTask(): Int {
        return exp_task
    }

    fun getDataComp(): Long? {
        return data_comp
    }

    fun getBreakTime(): Float? {
        return break_time
    }

    fun getStatusTask(): Boolean {
        return status_task
    }

    // Сеттеры
    fun setTitle(title: String) {
        this.title = title
    }

    fun setExpTask(expTask: Int) {
        exp_task = expTask
    }

    fun setDataComp(dataComp: Long?) {
        data_comp = dataComp!!
    }

    fun setBreakTime(breakTime: Float?) {
        break_time = breakTime
    }

    fun setStatusTask(statusTask: Boolean) {
        status_task = statusTask
    }
}