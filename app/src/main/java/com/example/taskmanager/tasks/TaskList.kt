package com.example.taskmanager.tasks

import com.example.taskmanager.dates.ExecutionPeriod
import com.example.taskmanager.dates.date_utils.doesDatesHaveSameDay
import com.example.taskmanager.utils.JsonAble
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

class TaskList(var tasks: HashSet<Task>? = HashSet(), var doSorting: Boolean = false) :
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


    fun updateSortedLists() {
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
        return doesDatesHaveSameDay(task.getExecutionPeriod().startDate, date)
    }


    fun update(){
        for(task in this.tasks!!){
            task.setExecutionPeriod(ExecutionPeriod( task.getCalendar(task.executionPeriodStartDate), task.getCalendar(task.executionPeriodEndDate)))
            task.setCreatingDate(task.getCalendar(task.creatingDateInLong))
        }
        for(task in this.tasksByDate!!){
            task.setExecutionPeriod(ExecutionPeriod( task.getCalendar(task.executionPeriodStartDate), task.getCalendar(task.executionPeriodEndDate)))
            task.setCreatingDate(task.getCalendar(task.creatingDateInLong))
        }
        for(task in this.tasksByExTime!!){
            task.setExecutionPeriod(ExecutionPeriod( task.getCalendar(task.executionPeriodStartDate), task.getCalendar(task.executionPeriodEndDate)))
            task.setCreatingDate(task.getCalendar(task.creatingDateInLong))
        }
    }



}