package com.example.taskmanager.dates

import com.example.taskmanager.dates.date_utils.doesDatesHaveSameDay
import com.example.taskmanager.dates.date_utils.formatMinutes
import java.util.*

class ExecutionPeriod(_startDate : Calendar = Calendar.getInstance(), _endDate : Calendar = Calendar.getInstance()){


    val startDate : Calendar

    val endDate : Calendar

    init {
        if (_startDate >= _endDate){
            startDate = Calendar.getInstance()
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.HOUR_OF_DAY, 1)
            endDate = calendar
        }
        else {
            startDate = _startDate
            endDate = _endDate
        }
    }

    override fun toString(): String {
        return if (doesDatesHaveSameDay(startDate, endDate))
            "${startDate[Calendar.HOUR_OF_DAY]}:${formatMinutes(startDate[Calendar.MINUTE])} - ${endDate[Calendar.HOUR_OF_DAY]}:${formatMinutes(endDate[Calendar.MINUTE])}"
        else
            "${startDate[Calendar.MONTH] + 1}.${startDate[Calendar.DAY_OF_MONTH]} " +
                    "${startDate[Calendar.HOUR_OF_DAY]}:${formatMinutes(startDate[Calendar.MINUTE])} " +
                    "- ${endDate[Calendar.MONTH] + 1}.${endDate[Calendar.DAY_OF_MONTH]} " +
                    "${endDate[Calendar.HOUR_OF_DAY]}:${formatMinutes(endDate[Calendar.MINUTE])}"
    }

}


