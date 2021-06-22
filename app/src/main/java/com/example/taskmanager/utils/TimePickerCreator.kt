package com.example.taskmanager.utils

import android.app.TimePickerDialog
import android.content.Context
import java.util.*

class TimePickerCreator(
    private val context: Context, private val calender: Calendar, private val onSetFunc : ()->Unit) {

    private val onSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
        calender[Calendar.HOUR_OF_DAY] = hourOfDay
        calender[Calendar.MINUTE] = minute
        onSetFunc()
    }

    fun setTime() {
        TimePickerDialog(
            context, onSetListener,
            calender[Calendar.HOUR_OF_DAY],
            calender[Calendar.MINUTE],
            true
        ).show()
    }

}