package com.example.taskmanager

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.taskmanager.dates.date_utils.formatMinutes
import com.example.taskmanager.tasks.TaskSnapshot
import com.example.taskmanager.ui.authorization.AuthorizationActivity
import com.example.taskmanager.users.Group
import com.example.taskmanager.utils.DatePickerCreator
import com.example.taskmanager.utils.TimePickerCreator
import java.util.*

class GroupTaskCreatingActivity : AppCompatActivity() {

    companion object {
        var group: Group = Group()
    }
    private var startTime = Calendar.getInstance()
    private var endTime = Calendar.getInstance()

    public override fun onCreate(savedInstance: Bundle?) {
        super.onCreate(savedInstance)
        setContentView(R.layout.activity_task_creating)
        val startText : TextView = findViewById(R.id.startTime)
        val dateText : TextView = findViewById(R.id.editTaskDate)
        val dateImg : ImageView = findViewById(R.id.dateChoosing)
        val endText : TextView = findViewById(R.id.endTime)
        val startImg : ImageView = findViewById(R.id.startChoosing)
        val endImg : ImageView = findViewById(R.id.endChoosing)
        startImg.setOnClickListener {
            TimePickerCreator(this, startTime){
                startText.text = "${startTime[Calendar.HOUR_OF_DAY]}:${formatMinutes(startTime[Calendar.MINUTE])}"
            }.setTime()
        }
        endImg.setOnClickListener {
            TimePickerCreator(this, endTime){
                endText.text = "${endTime[Calendar.HOUR_OF_DAY]}:${formatMinutes(endTime[Calendar.MINUTE])}"
            }.setTime()
        }
        dateImg.setOnClickListener {
            DatePickerCreator(this, startTime, endTime){
                dateText.text = "${startTime[Calendar.DAY_OF_MONTH]}.${startTime[Calendar.MONTH]+1}.${startTime[Calendar.YEAR]}"
            }.setDate()
        }


        val desc = findViewById<EditText>(R.id.editTask)
        val btn = findViewById<Button>(R.id.saveBtn)
        btn.setOnClickListener {
            val taskSnapshot = TaskSnapshot(desc.text.toString(), startTime.timeInMillis, endTime.timeInMillis)
            group.addTask(taskSnapshot)
            startActivity(Intent(this, AuthorizationActivity::class.java))
        }
    }
}