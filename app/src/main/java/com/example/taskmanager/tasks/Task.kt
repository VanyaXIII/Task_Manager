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
import com.google.firebase.database.Exclude
import java.util.*
import kotlin.collections.ArrayList

@JsonIgnoreProperties(ignoreUnknown = true)
data class Task(@JsonIgnore private val _description: String = "Nothing to do") : JsonAble {

    @JsonProperty("taskDescription")
    val description: String = _description

    @JsonProperty("emails")
    var emails: ArrayList<String> = ArrayList()

    @JsonProperty("ids")
    var ids: ArrayList<String> = ArrayList()

    @JsonProperty("dateOfCreating")
    @Exclude private var creatingDate: Calendar = Calendar.getInstance()

    @JsonProperty("priority")
    var isPriority: Boolean = false

    @JsonProperty("timePeriodToExecuteTask")
    @Exclude private var executionPeriod: ExecutionPeriod = ExecutionPeriod()

    @JsonProperty("creatingDateInLong")
    var creatingDateInLong: Long = creatingDate.timeInMillis

    @JsonProperty("executionPeriodStartDate")
    var executionPeriodStartDate: Long = executionPeriod.startDate.timeInMillis

    @JsonProperty("executionPeriodEndDate")
    var executionPeriodEndDate: Long = executionPeriod.endDate.timeInMillis

    @JsonProperty("isTaskDone")
    var isDone: Boolean = false


    @Exclude
    fun getCalendar(time: Long): Calendar{
        val res = Calendar.getInstance()
        res.timeInMillis = time
        return res
    }

    @JsonProperty("notificationParams")
    @Exclude private var notificationParams: NotificationTime = NotificationTime(this.executionPeriod.endDate, false)
        set(value) {
            field = if ((value.notificationTime > this.executionPeriod.endDate))
                NotificationTime(this.executionPeriod.endDate, false)
            else
                value
        }


    constructor(description: String, executionPeriod: ExecutionPeriod) : this(description) {
        this.executionPeriod = executionPeriod
        this.executionPeriodStartDate = executionPeriod.startDate.timeInMillis
        this.executionPeriodEndDate = executionPeriod.endDate.timeInMillis
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
    @Exclude
    fun getTimeUntilEnding(): Calendar {
        return this.executionPeriod.endDate.minus(Calendar.getInstance())
    }


    @Exclude fun getCreatingDate(): Calendar{
        return creatingDate
    }

    @Exclude fun getExecutionPeriod(): ExecutionPeriod{
        return executionPeriod
    }

    @Exclude fun setCreatingDate(creatingDate: Calendar){
        this.creatingDate = creatingDate
    }

    @Exclude fun setExecutionPeriod(executionPeriod: ExecutionPeriod){
        this.executionPeriod = executionPeriod
        this.executionPeriodStartDate = executionPeriod.startDate.timeInMillis
        this.executionPeriodEndDate = executionPeriod.endDate.timeInMillis
    }

    @Exclude fun getNotificationParams(): NotificationTime{
        return notificationParams
    }

    @JvmName("setNotificationParams1")
    @Exclude fun setNotificationParams(notificationParams: NotificationTime){
        this.notificationParams = notificationParams
    }



    fun makePriority() {
        isPriority = true
    }

    fun execute() {
        isDone = true
    }

    @Exclude
    fun doHaveSameDateWith(date: Calendar): Boolean {
        return doesDatesHaveSameDay(this.executionPeriod.startDate, date)
    }

    @Exclude
    override fun toJson(): String {
        val mapper = jacksonObjectMapper()
        return mapper.writeValueAsString(this)
    }


    @Exclude
    override fun toString(): String {
        val status = if (isDone) "Выполнено" else "Не выполнено"
        return "Описание: $description | Статус: $status \n" +
                "Время выполнения: ${this.executionPeriod}"
    }

    @Exclude
    override fun hashCode(): Int {
        return creatingDate.hashCode() //может рухнуть:)
    }

    @Exclude
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Task

        if (_description != other._description) return false
        if (description != other.description) return false
        if (creatingDate != other.creatingDate) return false
        if (isPriority != other.isPriority) return false
        if (this.executionPeriod != other.executionPeriod) return false
        if (isDone != other.isDone) return false

        return true
    }

}


