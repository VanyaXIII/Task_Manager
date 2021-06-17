package com.example.taskmanager.utils

import android.content.Context
import com.example.taskmanager.tasks.Task
import com.example.taskmanager.tasks.TaskList
import com.example.taskmanager.tasks.TasksFileManager
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

fun tasksToStrings(tasks:  ArrayList<Task>) : ArrayList<String>{
    if (tasks.size == 0) return ArrayList()
    val res = ArrayList<String>(tasks.size)
    for (i in 0 until tasks.size)
        res.add(tasks[i].toString())
    return res
}

fun initTestingTasks(tasksAsJson : String, context : Context){
    val mapper = jacksonObjectMapper()
    val tasks : ArrayList<Task> = mapper.readValue(tasksAsJson)
    TasksFileManager(TaskList(tasks), context).save()
}