package com.example.taskmanager.utils

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import java.util.*

class DatePickerCreator(
    private val context: Context, private vararg val calenders: Calendar, private val onSetFunc : ()->Unit) {


    private val onSetListener =
        OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            for (calender in calenders) {
                calender[Calendar.YEAR] = year
                calender[Calendar.MONTH] = monthOfYear
                calender[Calendar.DAY_OF_MONTH] = dayOfMonth
            }
            onSetFunc()
        }

    fun setDate() {
        DatePickerDialog(
            context, onSetListener,
            calenders[0][Calendar.YEAR],
            calenders[0][Calendar.MONTH],
            calenders[0][Calendar.DAY_OF_MONTH]
        ).show()
    }

}