package com.example.taskmanager

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.taskmanager.tasks.GroupTaskChecker
import com.example.taskmanager.tasks.TaskList
import com.example.taskmanager.tasks.TaskManager
import com.example.taskmanager.tasks.TasksFileHandler
import com.example.taskmanager.ui.authorization.AuthorizationActivity
import com.example.taskmanager.ui.task_creating.TaskCreatingActivity
import com.example.taskmanager.ui.task_list.ChoosingParams
import com.example.taskmanager.ui.task_list.ViewPagerAdapter
import com.example.taskmanager.utils.DatePickerCreator
import java.util.*


class MainActivity : AppCompatActivity() {


    private var date = Calendar.getInstance()
    private var taskList = TaskList(TasksFileHandler(null, this).load())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)
        val userAuth = findViewById<ImageView>(R.id.userAuth)
        userAuth.setOnClickListener { startActivity(Intent(this, AuthorizationActivity::class.java)) }
        taskList = TaskList(TasksFileHandler(null, this).load())
        val taskManager = TaskManager(taskList)
        val viewPager = findViewById<ViewPager2>(R.id.viewPager)
        viewPager.adapter = ViewPagerAdapter(taskManager, taskList)
        viewPager.currentItem = 50
        GroupTaskChecker(taskList, taskManager, this){
            val i = viewPager.currentItem
            viewPager.adapter = ViewPagerAdapter(taskManager, taskList)
            viewPager.currentItem = i
        }.start()
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
                if (item == "По выполнению") {
                    ViewPagerAdapter.choosingParams = ChoosingParams.BY_EX_TIME
                    viewPager.adapter = ViewPagerAdapter(taskManager, taskList)
                    val a = date[Calendar.DAY_OF_YEAR] - ViewPagerAdapter.date[Calendar.DAY_OF_YEAR]
                    viewPager.currentItem = a
                }
                if (item == "По добавлению") {
                    ViewPagerAdapter.choosingParams = ChoosingParams.BY_DATE
                    viewPager.adapter = ViewPagerAdapter(taskManager, taskList)
                    val a = date[Calendar.DAY_OF_YEAR] - ViewPagerAdapter.date[Calendar.DAY_OF_YEAR]
                    viewPager.currentItem = a
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        spinner.onItemSelectedListener = itemSelectedListener
        val calendarButton = findViewById<ImageView>(R.id.calender)
        calendarButton.setOnClickListener {
            DatePickerCreator(this, date) {
                val a = date[Calendar.DAY_OF_YEAR] - ViewPagerAdapter.date[Calendar.DAY_OF_YEAR]
                viewPager.currentItem = a
            }.setDate()
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