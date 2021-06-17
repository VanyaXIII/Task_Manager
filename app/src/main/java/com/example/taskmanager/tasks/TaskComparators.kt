package com.example.taskmanager.tasks

class TaskComparators {

    companion object {

        val creatingDateComparator = Comparator<Task> { task1, task2 ->
            when {
                task1.creatingDate == task2.creatingDate -> return@Comparator 0
                task1.creatingDate > task2.creatingDate -> return@Comparator 1
                else -> return@Comparator -1
            }
        }

        val executionPeriodComparator = Comparator<Task> { task1, task2 ->
            when {
                task1.executionPeriod.startDate == task2.executionPeriod.startDate -> return@Comparator 0
                task1.executionPeriod.startDate > task2.executionPeriod.startDate -> return@Comparator 1
                else -> return@Comparator -1
            }
        }
    }

}