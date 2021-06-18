package com.example.taskmanager.tasks

import com.example.taskmanager.dates.date_utils.doesDatesHaveSameDay
import com.example.taskmanager.utils.JsonAble
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

class TaskList(var tasks: HashSet<Task>? = HashSet(), private val doSorting: Boolean = false) :
    JsonAble {

    var tasksByDate = ArrayList<Task>()
    var tasksByExTime = ArrayList<Task>()

    init {
        if (tasks == null)
            tasks = HashSet()
        updateSortedLists()
    }

    fun addTask(task: Task) {
        tasks?.add(task)
        updateSortedLists()
    }

    fun deleteTask(task: Task) {
        tasks?.remove(task)
        tasksByExTime.remove(task)
        tasksByDate.remove(task)
    }


    private fun updateSortedLists() {
        if (doSorting) {
            tasksByDate = ArrayList(tasks?.sortedWith(TaskComparators.creatingDateComparator))
            tasksByExTime = ArrayList(tasks?.sortedWith(TaskComparators.executionPeriodComparator))
        }
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
        return doesDatesHaveSameDay(task.executionPeriod.startDate, date)
    }


}