package com.example.taskmanager.users

import com.example.taskmanager.tasks.TaskSnapshot
import java.util.*
import kotlin.collections.ArrayList

class Group {

    var id: String = ""
        set(value) {
            if (field == "")
                field = value
        }

    var name: String = ""
        set(value) {
            if (field == "")
                field = value
        }

    var changes : ArrayList<Change> = ArrayList()


    constructor()


    constructor(id : String, name : String){
        this.id = id
        this.name = name
    }

    fun addTask(taskSnapshot: TaskSnapshot){
        changes.add(Change(Calendar.getInstance(), ChangeDesc.ADDED, taskSnapshot))
    }

    fun deleteTask(taskSnapshot: TaskSnapshot){
        changes.add(Change(Calendar.getInstance(), ChangeDesc.DELETED, taskSnapshot))
    }




}