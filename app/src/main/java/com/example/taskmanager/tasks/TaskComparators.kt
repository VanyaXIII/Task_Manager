package com.example.taskmanager.tasks

import java.util.*
import kotlin.Comparator

class TaskComparators {

    companion object {

        private val parameters = arrayOf(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH, Calendar.HOUR_OF_DAY, Calendar.MINUTE)

        val creatingDateComparator = Comparator<Task> { task1, task2 ->
            operator fun Task.compareTo(other: Task): Int {
                for (i in parameters) {
                    if (this.getCreatingDate()[i] > other.getCreatingDate()[i]) return 1
                    if (this.getCreatingDate()[i] < other.getCreatingDate()[i]) return -1
                }
                if (this.isPriority > other.isPriority) return 1
                if (this.isPriority < other.isPriority) return -1
                return 0
            }
            when {
                task1 > task2 -> return@Comparator 1
                task1 < task2 -> return@Comparator -1
                else -> return@Comparator 0
            }
        }

        val executionPeriodComparator = Comparator<Task> { task1, task2 ->
            operator fun Task.compareTo(other: Task): Int {
                for (i in parameters) {
                    if (this.getExecutionPeriod().startDate[i] > other.getExecutionPeriod().startDate[i]) return 1
                    if (this.getExecutionPeriod().startDate[i] < other.getExecutionPeriod().startDate[i]) return -1
                }
                if (this.isPriority > other.isPriority) return 1
                if (this.isPriority < other.isPriority) return -1
                return 0
            }
            when {

                task1 > task2 -> return@Comparator 1
                task1 < task2 -> return@Comparator -1
                else -> return@Comparator 0

            }
        }
    }

}