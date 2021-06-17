package com.example.taskmanager.tasks

import com.example.taskmanager.utils.ExecutionPeriod
import com.example.taskmanager.utils.JsonAble
import com.example.taskmanager.utils.minus
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

    override fun toJson(): String {
        val mapper = jacksonObjectMapper()
        return mapper.writeValueAsString(this)
    }

    override fun toString(): String {
        val status = if (isDone) "Выполнено" else "Не выполнено"
        val cal = getTimeUntilEnding()
        return "Описание: $description | Статус: $status \n" +
                "Время до конца выполнения ${cal[Calendar.HOUR_OF_DAY]}:${cal[Calendar.MINUTE]}"
    }


}


