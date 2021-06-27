package com.example.taskmanager.users

import com.example.taskmanager.tasks.TaskSnapshot
import java.util.*

class Change {

    var id : String = ""
    var description : String = ""
    var taskSnapshot = TaskSnapshot("", 0, 0)

    constructor()

    constructor(
        id : String,
        description: ChangeDesc,
        taskSnapshot: TaskSnapshot
    ) : this(id, description.toString(), taskSnapshot)

    constructor(id : String, description: String, taskSnapshot: TaskSnapshot){
        this.id = id
        this.description = description
        this.taskSnapshot = taskSnapshot
    }

}