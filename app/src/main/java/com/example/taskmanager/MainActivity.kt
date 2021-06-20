package com.example.taskmanager

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    private var taskList = TaskList(TasksFileHandler(null, this).load())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        taskList = TaskList(TasksFileHandler(null, this).load())
        var tasks: ArrayList<Task>
        val recyclerView: RecyclerView = findViewById(R.id.rv)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val taskManager = TaskManager(taskList)
        val spinner = findViewById<Spinner>(R.id.spinner)
        val sortingParams = resources.getStringArray(R.array.sorting_params)
        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, sortingParams)
        val itemSelectedListener: OnItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {

                val item = parent.getItemAtPosition(position) as String
                if (item == "По выполнению"){
                    tasks = taskManager.getTaskListByDay(date).tasksByExTime
                    recyclerView.adapter = RecycleViewTasksAdapter(tasks)
                }
                if (item == "По добавлению"){
                    tasks = taskManager.getTaskListByDay(date).tasksByDate
                    recyclerView.adapter = RecycleViewTasksAdapter(tasks)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        spinner.onItemSelectedListener = itemSelectedListener
        recyclerView.adapter = RecycleViewTasksAdapter(ArrayList(taskManager.getTaskListByDay(date).tasks))
        val calendarButton = findViewById<ImageView>(R.id.calender)
        calendarButton.setOnClickListener {
            DatePickerCreator(
                this,
                date,
                taskManager,
                recyclerView
            ).setDate()
        }
        val creatingBtn = findViewById<Button>(R.id.createTaskBtn)
        creatingBtn.setOnClickListener {
            startActivity(Intent(this, TaskCreatingActivity::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        TasksFileHandler(taskList, this).save()
    }

    override fun onStop() {
        super.onStop()
        TasksFileHandler(taskList, this).save()
    }

    override fun onPause() {
        super.onPause()
        TasksFileHandler(taskList, this).save()
    }
}