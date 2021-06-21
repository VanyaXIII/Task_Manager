package com.example.taskmanager.utils

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import androidx.viewpager2.widget.ViewPager2
import com.example.taskmanager.dates.date_utils.minus
import com.example.taskmanager.tasks.TaskManager
import com.example.taskmanager.ui.task_list.ViewPagerAdapter
import java.util.*

class DatePickerCreator(
    private val context: Context, private val calender: Calendar,
    private val taskManager: TaskManager? = null, private val list: ViewPager2? = null
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
        if ((list != null) and (taskManager != null)) {
            val tasks = taskManager!!.getTaskListByDay(calender).tasks
            if (tasks != null) {
                list!!.currentItem = calender.minus(ViewPagerAdapter.date)[Calendar.DAY_OF_YEAR] + 1
            }
        }
    }

}