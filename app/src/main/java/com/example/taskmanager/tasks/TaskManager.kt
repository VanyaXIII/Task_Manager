package com.example.taskmanager.tasks

import com.example.taskmanager.dates.date_utils.getDayAndYear
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet

class TaskManager(taskList: TaskList = TaskList(HashSet())) {

    private val tasksMap: HashMap<Pair<Int, Int>, TaskList> = HashMap()

    init {
        val mapOfSets: HashMap<Pair<Int, Int>, HashSet<Task>> = HashMap()
        for (task in taskList.tasks!!) {
            val taskDate = task.executionPeriod.startDate.getDayAndYear()
            if (mapOfSets[taskDate] == null) mapOfSets[taskDate] = HashSet()
            mapOfSets[taskDate]?.add(task)
        }
        for(key in mapOfSets.keys){
           tasksMap[key] = TaskList(mapOfSets[key], true)
        }
    }

    fun getTaskListByDay(date : Calendar) : TaskList{
        val dayAndYear = date.getDayAndYear()
        return if (tasksMap[dayAndYear] == null)
            TaskList(HashSet())
        else
            tasksMap[dayAndYear]!!
    }

    fun deleteTask(task: Task){
        getTaskListByDay(task.executionPeriod.startDate).deleteTask(task)
    }

}