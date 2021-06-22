package com.example.taskmanager.ui.task_creating

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.taskmanager.MainActivity
import com.example.taskmanager.R
import com.example.taskmanager.dates.ExecutionPeriod
import com.example.taskmanager.notifications.NotificationTime
import com.example.taskmanager.tasks.Task
import com.example.taskmanager.tasks.TaskList
import com.example.taskmanager.tasks.TasksFileHandler
import com.example.taskmanager.utils.DatePickerCreator
import com.example.taskmanager.utils.TimePickerCreator
import java.util.*


class TaskCreatingActivity : AppCompatActivity() {

    private var startTime = Calendar.getInstance()
    private var endTime = Calendar.getInstance()

    public override fun onCreate(savedInstance: Bundle?) {
        super.onCreate(savedInstance)
        setContentView(R.layout.activity_task_creating)
        val tasks = TasksFileHandler(null, this).load()
        val startText : TextView = findViewById(R.id.startTime)
        val dateText : TextView = findViewById(R.id.editTaskDate)
        val dateImg : ImageView = findViewById(R.id.dateChoosing)
        val endText : TextView = findViewById(R.id.endTime)
        val startImg : ImageView = findViewById(R.id.startChoosing)
        val endImg : ImageView = findViewById(R.id.endChoosing)
        startImg.setOnClickListener {
            TimePickerCreator(this, startTime){
                startText.text = "${startTime[Calendar.HOUR_OF_DAY]}:${startTime[Calendar.MINUTE]}"
            }.setTime()
        }
        endImg.setOnClickListener {
            TimePickerCreator(this, endTime){
                endText.text = "${endTime[Calendar.HOUR_OF_DAY]}:${endTime[Calendar.MINUTE]}"
            }.setTime()
        }
        dateImg.setOnClickListener {
            DatePickerCreator(this, startTime, endTime){
                dateText.text = "${startTime[Calendar.DAY_OF_MONTH]}.${startTime[Calendar.MONTH]+1}.${startTime[Calendar.YEAR]}"
            }.setDate()
        }


        val taskList = TaskList(tasks)
        val desc = findViewById<EditText>(R.id.editTask)
        val btn = findViewById<Button>(R.id.saveBtn)
        btn.setOnClickListener {
            val task = Task(desc.text.toString(), false, ExecutionPeriod(startTime, endTime), false, NotificationTime(endTime, false))
            taskList.addTask(task)
            TasksFileHandler(taskList, this).save()
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

}