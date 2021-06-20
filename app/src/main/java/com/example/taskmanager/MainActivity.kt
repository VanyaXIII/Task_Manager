package com.example.taskmanager

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanager.notifications.NotificationService
import com.example.taskmanager.tasks.Task
import com.example.taskmanager.tasks.TaskList
import com.example.taskmanager.tasks.TaskManager
import com.example.taskmanager.tasks.TasksFileHandler
import com.example.taskmanager.ui.CustomRecyclerAdapter
import com.example.taskmanager.utils.DatePickerCreator
import java.util.*

class MainActivity : AppCompatActivity() {

    private var date = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("Cale", Calendar.getInstance()[Calendar.MINUTE].toString())
        val taskList = TaskList(TasksFileHandler(null, this).load())
        val recyclerView: RecyclerView = findViewById(R.id.rv)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = CustomRecyclerAdapter(fillList(taskList))
        val taskManager = TaskManager(taskList)
        val calendarButton= findViewById<Button>(R.id.calendarButton)
        //val list : ListView = findViewById(R.id.listView)
        //calendarButton.setOnClickListener { DatePickerCreator(this, date, taskManager, list).setDate()}
        var tasks: ArrayList<Task>
        val addingBtn = findViewById<Button>(R.id.addingBtn)
        val executingBtn = findViewById<Button>(R.id.executingBtn)
        /*addingBtn.setOnClickListener { tasks = taskManager.getTaskListByDay(date).tasksByDate
            list.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, tasks)}*/
        /*executingBtn.setOnClickListener {  tasks = taskManager.getTaskListByDay(date).tasksByExTime
            list.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, tasks)}*/
        val creatingBtn = findViewById<Button>(R.id.createTaskBtn)
        creatingBtn.setOnClickListener {
            startActivity(Intent(this, TaskCreatingActivity::class.java))
        }
    }

    private fun fillList(taskList: TaskList): List<String> {
        var tasks: ArrayList<String> = ArrayList()
            taskList.tasks!!.forEach { tasks.add(it.description) }
        return tasks
    }

    private fun startNotificationService(){
        val serviceIntent  = Intent(this, NotificationService::class.java)
        startService(serviceIntent)
    }
}