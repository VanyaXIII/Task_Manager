package com.example.taskmanager.utils

import com.example.taskmanager.tasks.Task

fun tasksToStrings(tasks:  ArrayList<Task>) : ArrayList<String>{
    if (tasks.size == 0) return ArrayList()
    val res = ArrayList<String>(tasks.size)
    for (i in 0 until tasks.size)
        res.add(tasks[i].toString())
    return res
}
