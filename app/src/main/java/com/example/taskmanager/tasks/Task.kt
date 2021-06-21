package com.example.taskmanager.tasks

import com.example.taskmanager.dates.ExecutionPeriod
import com.example.taskmanager.dates.date_utils.doesDatesHaveSameDay
import com.example.taskmanager.dates.date_utils.minus
import com.example.taskmanager.notifications.NotificationTime
import com.example.taskmanager.users.User
import com.example.taskmanager.utils.JsonAble
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.util.*
import kotlin.collections.ArrayList

@JsonIgnoreProperties(ignoreUnknown = true)
data class Task(@JsonIgnore private val _description: String = "Nothing to do") : JsonAble {

    @JsonProperty("taskDescription")
    val description: String = _description

    var emails: ArrayList<String> = ArrayList()

    var ids: ArrayList<String> = ArrayList()

    @JsonProperty("dateOfCreating")
    val creatingDate: Calendar = Calendar.getInstance()

    @JsonProperty("priority")
    var isPriority: Boolean = false

    @JsonProperty("timePeriodToExecuteTask")
    var executionPeriod: ExecutionPeriod = ExecutionPeriod()

    @JsonProperty("isTaskDone")
    var isDone: Boolean = false

    @JsonProperty("notificationParams")
    var notificationParams: NotificationTime = NotificationTime(executionPeriod.endDate, false)
        set(value) {
            field = if ((value.notificationTime > executionPeriod.endDate))
                NotificationTime(this.executionPeriod.endDate, false)
            else
                value
        }

    constructor(description: String, executionPeriod: ExecutionPeriod) : this(description) {
        this.executionPeriod = executionPeriod
        val date = executionPeriod.endDate.clone() as Calendar
        date.add(Calendar.MINUTE, -15)
        notificationParams = NotificationTime(date, false)
    }

    constructor(
        description: String,
        isPriority: Boolean,
        executionPeriod: ExecutionPeriod,
        isDone : Boolean,
        notificationTime: NotificationTime,

    ) : this(description, executionPeriod) {
        this.isPriority = isPriority
        this.notificationParams = notificationTime
        this.isDone = isDone
    }

    //TODO другие конструкторы

    @JsonIgnore
    fun getTimeUntilEnding(): Calendar {
        return executionPeriod.endDate.minus(Calendar.getInstance())
    }

    fun makePriority() {
        isPriority = true
    }

    fun execute() {
        isDone = true
    }

    fun doHaveSameDateWith(date: Calendar): Boolean {
        return doesDatesHaveSameDay(executionPeriod.startDate, date)
    }

    override fun toJson(): String {
        val mapper = jacksonObjectMapper()
        return mapper.writeValueAsString(this)
    }

    override fun toString(): String {
        val status = if (isDone) "Выполнено" else "Не выполнено"
        return "Описание: $description | Статус: $status \n" +
                "Время выполнения: $executionPeriod"
    }

    override fun hashCode(): Int {
        return creatingDate.hashCode() //может рухнуть:)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Task

        if (_description != other._description) return false
        if (description != other.description) return false
        if (creatingDate != other.creatingDate) return false
        if (isPriority != other.isPriority) return false
        if (executionPeriod != other.executionPeriod) return false
        if (isDone != other.isDone) return false

        return true
    }

}


