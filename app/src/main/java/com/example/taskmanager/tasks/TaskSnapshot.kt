package com.example.taskmanager.tasks

import com.example.taskmanager.dates.ExecutionPeriod
import java.util.*

class TaskSnapshot {


    var description: String = ""
    var startTime : Long = 0
    var endTime : Long = 0

    constructor()

    constructor(description : String, startTime : Long, endTime : Long){
        this.description = description
        this.startTime = startTime
        this.endTime = endTime
    }

    val creatingTime : Long = Calendar.getInstance().timeInMillis


    override fun hashCode(): Int {
        var result = description.hashCode()
        result = 31 * result + (startTime.hashCode())
        result = 31 * result + (endTime.hashCode()  )
        result = 31 * result + creatingTime.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TaskSnapshot

        if (description != other.description) return false
        if (startTime != other.startTime) return false
        if (endTime != other.endTime) return false
        if (creatingTime != other.creatingTime) return false

        return true
    }

    fun toTask() : Task{
        val startDate = Calendar.getInstance()
        startDate.timeInMillis = startTime
        val endDate = Calendar.getInstance()
        endDate.timeInMillis = endTime
        return Task(description, ExecutionPeriod(startDate, endDate))
    }

}