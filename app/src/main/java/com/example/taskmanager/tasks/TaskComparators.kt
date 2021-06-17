package com.example.taskmanager.tasks

import java.util.*
import kotlin.Comparator

class TaskComparators {

    companion object {

        private val parameters = arrayOf(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH, Calendar.HOUR_OF_DAY, Calendar.MINUTE)

        val creatingDateComparator = Comparator<Task> { task1, task2 ->
            operator fun Task.compareTo(other: Task): Int {
                for (i in parameters) {
                    if (this.creatingDate[i] > other.creatingDate[i]) return 1
                    if (this.creatingDate[i] < other.creatingDate[i]) return -1
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
                    if (this.executionPeriod.startDate[i] > other.executionPeriod.startDate[i]) return 1
                    if (this.executionPeriod.startDate[i] < other.executionPeriod.startDate[i]) return -1
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