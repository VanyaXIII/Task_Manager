package com.example.taskmanager.tasks

import com.example.taskmanager.utils.JsonAble
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.util.*
import kotlin.collections.ArrayList

class TaskList(var tasks: ArrayList<Task> = ArrayList()) : JsonAble {

    var tasksByDate = ArrayList<Task>()
    var tasksByExTime = ArrayList<Task>()

    init {
        updateSortedLists()
    }

    constructor(tasks: ArrayList<Task>, date: Calendar) : this(tasks) {
        this.tasks = ArrayList(tasks.filter { doHaveSameDays(it, date) })
        updateSortedLists()
    }

    fun addTask(task: Task) {
        tasks.add(task)
        updateSortedLists()
    }

    fun deleteTask(task: Task) {
        tasks.remove(task)
        tasksByExTime.remove(task)
        tasksByDate.remove(task)
    }

    fun getTaskListByDay(date: Calendar) : TaskList{
        val res = TaskList()
        res.tasks = ArrayList(tasks.filter { doHaveSameDays(it, date) })
        res.tasksByDate = ArrayList(tasksByDate.filter { doHaveSameDays(it, date)})
        res.tasksByExTime = ArrayList(tasksByExTime.filter { doHaveSameDays(it, date)})
        return res
    }

    private fun updateSortedLists() {
        tasksByDate = ArrayList(tasks.sortedWith(TaskComparators.creatingDateComparator))
        tasksByExTime = ArrayList(tasks.sortedWith(TaskComparators.executionPeriodComparator))
    }

    fun getTasksAsJson(): String {
        val mapper = jacksonObjectMapper()
        return mapper.writeValueAsString(tasks)
    }

    override fun toJson(): String {
        val mapper = jacksonObjectMapper();
        return mapper.writeValueAsString(this)
    }

    private fun doHaveSameDays(task: Task, date: Calendar): Boolean {
        return (task.executionPeriod.startDate.get(Calendar.DAY_OF_YEAR) == date.get(Calendar.DAY_OF_YEAR)) and
                (task.executionPeriod.startDate.get(Calendar.YEAR) == date.get(Calendar.YEAR))
    }


}