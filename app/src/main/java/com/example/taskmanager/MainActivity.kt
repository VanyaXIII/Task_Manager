package com.example.taskmanager

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CalendarView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.taskmanager.tasks.TaskList
import com.example.taskmanager.utils.TasksFileManager
import com.example.taskmanager.utils.tasksToStrings
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val taskList = TaskList(TasksFileManager(null, this).load())
        val calendar : CalendarView = findViewById(R.id.calendarView)
        val list : ListView = findViewById(R.id.listView)
        var tasks: ArrayList<String>
        var c : Calendar = Calendar.getInstance()
        calendar.setOnDateChangeListener { _, year, month, dayOfMonth ->  c = GregorianCalendar(year, month, dayOfMonth)
            tasks = tasksToStrings(taskList.getTaskListByDay(c).tasks)
            list.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, tasks)
        }
        val addingBtn = findViewById<Button>(R.id.addingBtn)
        val executingBtn = findViewById<Button>(R.id.executingBtn)
        addingBtn.setOnClickListener { tasks = tasksToStrings(taskList.getTaskListByDay(c).tasksByDate)
            list.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, tasks)}
        executingBtn.setOnClickListener {  tasks = tasksToStrings(taskList.getTaskListByDay(c).tasksByExTime)
            list.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, tasks)}
    }
}