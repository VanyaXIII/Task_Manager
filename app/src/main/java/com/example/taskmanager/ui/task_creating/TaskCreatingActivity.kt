package com.example.taskmanager.ui.task_creating

import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.taskmanager.MainActivity
import com.example.taskmanager.R
import com.example.taskmanager.dates.ExecutionPeriod
import com.example.taskmanager.tasks.Task
import com.example.taskmanager.tasks.TaskList
import com.example.taskmanager.tasks.TasksFileHandler
import com.example.taskmanager.users.AuthManager
import com.example.taskmanager.users.User
import com.example.taskmanager.utils.DatePickerCreator
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class TaskCreatingActivity : AppCompatActivity() {

    private var dateAndTime = Calendar.getInstance()
    private var id = HashMap<String,String>() // id[user_email] = user_id
    private var shouldAddTask = false
    private var taskToAdd = Task()
    val tasks = TasksFileHandler(null, this).load()

    public override fun onCreate(savedInstance: Bundle?) {
        super.onCreate(savedInstance)
        setContentView(R.layout.activity_task_creating)
        shouldAddTask = false
        DatePickerCreator(this, dateAndTime).setDate()
        setTime()
        val desc = findViewById<EditText>(R.id.editTaskDescription)
        val addButton = findViewById<Button>(R.id.addButton)
        val email = findViewById<EditText>(R.id.email)
        val btn = findViewById<Button>(R.id.saveBtn)
        val emails: ArrayList<String> = ArrayList()

        btn.setOnClickListener {
            val a : Calendar = dateAndTime.clone() as Calendar
            a.add(Calendar.HOUR_OF_DAY, 2)
            val task = Task(desc.text.toString(), ExecutionPeriod(dateAndTime, a))
            task.emails = emails
            task.emails.add(FirebaseAuth.getInstance().currentUser!!.email.toString())
            shouldAddTask = true
            taskToAdd = task
            startActivity(Intent(this, MainActivity::class.java))
        }

        addButton.setOnClickListener{
            val personEmail = email.text.toString()
            if(isEmailValid(personEmail) && !emails.contains(personEmail)){
                emails.add(personEmail)
            }
        }
        val dataBase = FirebaseDatabase.getInstance().getReference("Users")
        dataBase.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(i in snapshot.children){
                    val user = i.getValue(User::class.java)
                    id[user!!.getEmail().toString()] = user!!.getUid().toString()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("cancel", error.toString())
            }

        })
    }



    fun isEmailValid(email: String): Boolean {
        return email.contains("@") and email.contains(".")
    }

    override fun onStop() {
        super.onStop()
        if(shouldAddTask) {
            val ids = ArrayList<String>()
            for (i in taskToAdd.emails) {
                if (id[i] == null) {
                    taskToAdd.emails.remove(i)
                } else {
                    ids.add(id[i]!!)
                }
            }
            taskToAdd.ids = ids
            val dataBase = FirebaseDatabase.getInstance().getReference("Users")
            dataBase.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (i in snapshot.children) {
                        val user = i.getValue(User::class.java)
                        if (taskToAdd.ids.contains(user!!.getUid())) {
                            user.tasks.add(taskToAdd)
                            dataBase.child(user.getUid().toString()).setValue(user)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("cancel", error.toString())
                }

            })
            val taskList = TaskList(tasks)
            taskList.addTask(taskToAdd)
            taskList.update()
            TasksFileHandler(taskList, this).save()
            val dataBaseTasks = FirebaseDatabase.getInstance().getReference("Tasks")
            dataBaseTasks.push().setValue(taskToAdd)
            shouldAddTask = false
            return
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