package com.example.taskmanager.users

import com.example.taskmanager.tasks.TaskSnapshot
import java.util.*

class Change(val timeStamp: Long?, val description: String?, val taskSnapshot: TaskSnapshot?) {

    constructor(
        calendar: Calendar = Calendar.getInstance(),
        description: String?,
        taskSnapshot: TaskSnapshot?
    ) : this(calendar.timeInMillis, description, taskSnapshot)

    constructor(
        calendar: Calendar = Calendar.getInstance(),
        description: ChangeDesc?,
        taskSnapshot: TaskSnapshot?
    ) : this(calendar.timeInMillis, description.toString(), taskSnapshot)

}