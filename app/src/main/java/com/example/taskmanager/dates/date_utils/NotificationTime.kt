package com.example.taskmanager.dates.date_utils

import com.example.taskmanager.tasks.Task
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

class NotificationTime(task: Task){

    @JsonProperty("timeToNotify")
    var notificationTime : Calendar = task.executionPeriod.endDate.clone() as Calendar
    var notified : Boolean = false

    init {
        notificationTime.add(Calendar.MINUTE, -15)
    }

    override fun toString(): String {
        return "${notificationTime[Calendar.DAY_OF_MONTH]} ${notificationTime[Calendar.HOUR_OF_DAY]}:${notificationTime[Calendar.MINUTE]}"
    }

}