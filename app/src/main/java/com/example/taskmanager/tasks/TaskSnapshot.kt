package com.example.taskmanager.tasks

import java.util.*

data class TaskSnapshot(val description: String? = "", val startTime : Long?, val endTime : Long?) {

    val creatingTime : Long = Calendar.getInstance().timeInMillis


    override fun hashCode(): Int {
        var result = description?.hashCode() ?: 0
        result = 31 * result + (startTime?.hashCode() ?: 0)
        result = 31 * result + (endTime?.hashCode() ?: 0)
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

}