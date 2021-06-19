package com.example.taskmanager

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.taskmanager.tasks.Task
import com.example.taskmanager.tasks.TaskList
import com.example.taskmanager.tasks.TaskManager
import com.example.taskmanager.tasks.TasksFileHandler
import com.example.taskmanager.utils.DatePickerCreator
import java.util.*

class MainActivity : AppCompatActivity() {

    private var date = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val taskList = TaskList(TasksFileHandler(null, this).load())
        val taskManager = TaskManager(taskList)
        val calendarButton= findViewById<Button>(R.id.calendarButton)
        val list : ListView = findViewById(R.id.listView)
        calendarButton.setOnClickListener { DatePickerCreator(this, date, taskManager, list).setDate()}
        var tasks: ArrayList<Task>
        val addingBtn = findViewById<Button>(R.id.addingBtn)
        val executingBtn = findViewById<Button>(R.id.executingBtn)
        addingBtn.setOnClickListener { tasks = taskManager.getTaskListByDay(date).tasksByDate
            list.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, tasks)}
        executingBtn.setOnClickListener {  tasks = taskManager.getTaskListByDay(date).tasksByExTime
            list.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, tasks)}
        val creatingBtn = findViewById<Button>(R.id.createTaskBtn)
        creatingBtn.setOnClickListener {
            startActivity(Intent(this, TaskCreatingActivity::class.java))
        }
    }

}