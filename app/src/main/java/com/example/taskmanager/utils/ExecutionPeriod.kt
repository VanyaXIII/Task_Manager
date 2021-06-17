package com.example.taskmanager.utils

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


}


