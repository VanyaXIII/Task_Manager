package com.example.taskmanager.tasks

import com.example.taskmanager.dates.ExecutionPeriod
import com.example.taskmanager.dates.date_utils.doesDatesHaveSameDay
import com.example.taskmanager.dates.date_utils.minus
import com.example.taskmanager.utils.JsonAble
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.util.*


data class Task(@JsonIgnore private val _description: String = "Nothing to do") : JsonAble {

    @JsonProperty("taskDescription")
    val description: String = _description

    @JsonProperty("dateOfCreating")
    val creatingDate: Calendar = Calendar.getInstance()

    @JsonProperty("priority")
    var isPriority: Boolean = false

    @JsonProperty("timePeriodToExecuteTask")
    var executionPeriod: ExecutionPeriod = ExecutionPeriod()

    @JsonProperty("isTaskDone")
    var isDone: Boolean = false

    constructor(description: String, executionPeriod: ExecutionPeriod) : this(description) {
        this.executionPeriod = executionPeriod
    }

    private fun getTimeUntilEnding() : Calendar {
       return executionPeriod.endDate - executionPeriod.startDate
    }

    fun makePriority() {
        isPriority = true
    }

    fun execute() {
        isDone = true
    }

    fun doHaveSameDateWith(date : Calendar) : Boolean{
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

}


