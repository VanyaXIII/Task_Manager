package com.example.taskmanager

import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.taskmanager.dates.ExecutionPeriod
import com.example.taskmanager.tasks.Task
import com.example.taskmanager.tasks.TaskList
import com.example.taskmanager.tasks.TasksFileHandler
import com.example.taskmanager.utils.DatePickerCreator
import java.util.*


class TaskCreatingActivity : AppCompatActivity() {

    private var dateAndTime = Calendar.getInstance()

    public override fun onCreate(savedInstance: Bundle?) {
        super.onCreate(savedInstance)
        setContentView(R.layout.activity_task_creating)
        DatePickerCreator(this, dateAndTime).setDate()
        setTime()
        val tasks = TasksFileHandler(null, this).load()
        val taskList = TaskList(tasks)
        val desc = findViewById<EditText>(R.id.editTaskDescription)
        val backBtn = findViewById<Button>(R.id.toListBtn)
        backBtn.setOnClickListener { startActivity(Intent(this, MainActivity::class.java)) }
        val btn = findViewById<Button>(R.id.saveBtn)
        btn.setOnClickListener {
            val a : Calendar = dateAndTime.clone() as Calendar
            a.add(Calendar.HOUR_OF_DAY, 2)
            val task = Task(desc.text.toString(), ExecutionPeriod(dateAndTime, a))
            taskList.addTask(task)
            TasksFileHandler(taskList, this).save()
            Log.d("Test", task.toJson())
        }
    }


    private fun setTime() {
        TimePickerDialog(
            this, t,
            dateAndTime[Calendar.HOUR_OF_DAY],
            dateAndTime[Calendar.MINUTE], true
        )
            .show()
    }

    private var t =
        OnTimeSetListener { _, hourOfDay, minute ->
            dateAndTime[Calendar.HOUR_OF_DAY] = hourOfDay
            dateAndTime[Calendar.MINUTE] = minute
        }
}