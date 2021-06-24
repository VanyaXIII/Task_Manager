package com.example.taskmanager.users

import com.example.taskmanager.tasks.TaskSnapshot
import java.util.*

class Change {

    var timeStamp : Long = 0
    var description : String = ""
    var taskSnapshot = TaskSnapshot("", 0, 0)

    constructor()

    constructor(
        calendar: Calendar = Calendar.getInstance(),
        description: String,
        taskSnapshot: TaskSnapshot,
    ) : this(calendar.timeInMillis, description, taskSnapshot)

    constructor(
        calendar: Calendar = Calendar.getInstance(),
        description: ChangeDesc,
        taskSnapshot: TaskSnapshot
    ) : this(calendar.timeInMillis, description.toString(), taskSnapshot)

    constructor(timeStamp: Long, description: String, taskSnapshot: TaskSnapshot){
        this.timeStamp = timeStamp
        this.description = description
        this.taskSnapshot = taskSnapshot
    }

}