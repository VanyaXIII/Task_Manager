package com.example.taskmanager

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanager.notifications.NotificationService
import com.example.taskmanager.tasks.Task
import com.example.taskmanager.tasks.TaskList
import com.example.taskmanager.tasks.TaskManager
import com.example.taskmanager.tasks.TasksFileHandler
import com.example.taskmanager.ui.RecycleViewTasksAdapter
import com.example.taskmanager.utils.DatePickerCreator
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private var date = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val taskList = TaskList(TasksFileHandler(null, this).load())
        val recyclerView: RecyclerView = findViewById(R.id.rv)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val taskManager = TaskManager(taskList)
        val calendarButton = findViewById<Button>(R.id.calendarButton)
        calendarButton.setOnClickListener {
            DatePickerCreator(
                this,
                date,
                taskManager,
                recyclerView
            ).setDate()
        }
        var tasks: ArrayList<Task>
        val addingBtn = findViewById<Button>(R.id.addingBtn)
        val executingBtn = findViewById<Button>(R.id.executingBtn)
        addingBtn.setOnClickListener {
            tasks = taskManager.getTaskListByDay(date).tasksByDate
            recyclerView.adapter = RecycleViewTasksAdapter(tasks)
        }
        executingBtn.setOnClickListener {
            tasks = taskManager.getTaskListByDay(date).tasksByExTime
            recyclerView.adapter = RecycleViewTasksAdapter(tasks)
        }
        val creatingBtn = findViewById<Button>(R.id.createTaskBtn)
        creatingBtn.setOnClickListener {
            startActivity(Intent(this, TaskCreatingActivity::class.java))
        }
    }

    companion object {
        fun fillList(tasks: ArrayList<Task>): List<String> {
            val stringList: ArrayList<String> = ArrayList()
            tasks.forEach { stringList.add(it.description) }
            return stringList
        }
    }

    private fun startNotificationService() {
        val serviceIntent = Intent(this, NotificationService::class.java)
        startService(serviceIntent)
    }
}