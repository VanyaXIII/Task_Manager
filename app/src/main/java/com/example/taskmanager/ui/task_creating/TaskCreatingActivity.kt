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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class TaskCreatingActivity : AppCompatActivity() {

    private var dateAndTime = Calendar.getInstance()

    public override fun onCreate(savedInstance: Bundle?) {
        super.onCreate(savedInstance)
        setContentView(R.layout.activity_task_creating)
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        DatePickerCreator(this, dateAndTime).setDate()
        setTime()
        val tasks = TasksFileHandler(null, this).load(uid)
        val taskList = TaskList(tasks)
        val desc = findViewById<EditText>(R.id.editTaskDescription)
        val addButton = findViewById<Button>(R.id.addButton)
        val email = findViewById<EditText>(R.id.email)
        val btn = findViewById<Button>(R.id.saveBtn)
        val emails: ArrayList<String> = ArrayList()
        val id: HashMap<String, String> = HashMap()
        val existingEmails: ArrayList<String> = ArrayList()

        btn.setOnClickListener {
            val a : Calendar = dateAndTime.clone() as Calendar
            a.add(Calendar.HOUR_OF_DAY, 2)
            val task = Task(desc.text.toString(), ExecutionPeriod(dateAndTime, a))
            emails.add(FirebaseAuth.getInstance().currentUser!!.email.toString())
            task.emails = emails
            val ids: ArrayList<String> = ArrayList()
            for(i in emails) ids.add(id[i]!!)
            task.ids = ids
            taskList.addTask(task)
            TasksFileHandler(taskList, this).save()
            TasksFileHandler(taskList, this).add(task)
            startActivity(Intent(this, MainActivity::class.java))
        }

        addButton.setOnClickListener{
            val personEmail = email.text.toString()
            if(isEmailValid(personEmail) && !emails.contains(personEmail) && existingEmails.contains(personEmail)){
                emails.add(personEmail)
            }
        }
        val dataBase = FirebaseDatabase.getInstance().getReference("Users")
        dataBase.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(i in snapshot.children){
                    val user = i.getValue(User::class.java)
                    val email1: String = user!!.getEmail().toString()
                    val id1: String = user!!.getUid().toString()
                    id[email1] = id1
                    existingEmails.add(email1)
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