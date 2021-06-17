package com.example.taskmanager

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.taskmanager.tasks.Task
import com.example.taskmanager.tasks.TaskList
import com.example.taskmanager.utils.ExecutionPeriod
import com.example.taskmanager.utils.TasksFileManager
import java.util.*


class TaskCreatingActivity : AppCompatActivity() {

    private var dateAndTime = Calendar.getInstance()

    public override fun onCreate(savedInstance: Bundle?) {
        super.onCreate(savedInstance)
        setContentView(R.layout.activity_task_creating)
        setDate()
        setTime()
        val tasks = TasksFileManager(null, this).load()
        val taskList = TaskList(tasks)
        val desc = findViewById<EditText>(R.id.editTaskDescription)
        val btn = findViewById<Button>(R.id.saveBtn)
        btn.setOnClickListener {
            val a : Calendar = dateAndTime.clone() as Calendar
            a.add(Calendar.HOUR_OF_DAY, 2)
            val task = Task(desc.text.toString(), ExecutionPeriod(dateAndTime, a))
            taskList.addTask(task)
            TasksFileManager(taskList, this).save()
            Log.d("Test", task.toJson())
        }
    }

    fun setDate() {
        DatePickerDialog(
            this, d,
            dateAndTime[Calendar.YEAR],
            dateAndTime[Calendar.MONTH],
            dateAndTime[Calendar.DAY_OF_MONTH]
        )
            .show()
    }

    fun setTime() {
        TimePickerDialog(
            this, t,
            dateAndTime[Calendar.HOUR_OF_DAY],
            dateAndTime[Calendar.MINUTE], true
        )
            .show()
    }

    var t =
        OnTimeSetListener { view, hourOfDay, minute ->
            dateAndTime[Calendar.HOUR_OF_DAY] = hourOfDay
            dateAndTime[Calendar.MINUTE] = minute
        }

    var d =
        OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            dateAndTime[Calendar.YEAR] = year
            dateAndTime[Calendar.MONTH] = monthOfYear
            dateAndTime[Calendar.DAY_OF_MONTH] = dayOfMonth
        }
}