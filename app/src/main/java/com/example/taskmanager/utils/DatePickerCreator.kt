package com.example.taskmanager.utils

import android.R
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.widget.ArrayAdapter
import android.widget.ListView
import com.example.taskmanager.tasks.TaskList
import java.util.*

class DatePickerCreator(
    private val context: Context, private val calender: Calendar,
    private val taskList: TaskList? = null, private val list: ListView? = null
) {


    private val onSetListener =
        OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            calender[Calendar.YEAR] = year
            calender[Calendar.MONTH] = monthOfYear
            calender[Calendar.DAY_OF_MONTH] = dayOfMonth
            updateListView()
        }

    fun setDate() {
        DatePickerDialog(
            context, onSetListener,
            calender[Calendar.YEAR],
            calender[Calendar.MONTH],
            calender[Calendar.DAY_OF_MONTH]
        ).show()
    }

    private fun updateListView(){
        if ((list != null) and (taskList != null)) {
            val tasks = taskList!!.getTaskListByDay(calender).tasks
            list!!.adapter = ArrayAdapter(context, R.layout.simple_list_item_1, tasks)
        }
    }

}