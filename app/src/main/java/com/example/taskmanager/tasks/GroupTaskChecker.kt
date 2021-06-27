package com.example.taskmanager.tasks

import android.content.Context
import com.example.taskmanager.database.RealtimeDatabase
import com.example.taskmanager.users.Group
import com.example.taskmanager.users.User
import com.example.taskmanager.utils.ChangesFileHandler
import com.example.taskmanager.utils.TimeStamp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

class GroupTaskChecker(
    private val taskList: TaskList,
    private val taskManager: TaskManager,
    private val context: Context,
    private val onTasksAdded : () -> Unit = {}
) {

    private var currentUser = FirebaseAuth.getInstance().currentUser
    private val mapOfChanges = ChangesFileHandler(null, context).load()

    fun start(){
        Thread{
            runBlocking {
                launch { updateCurrentUser() }
                launch { load() }
            }
        }.start()
    }

    private suspend fun updateCurrentUser(){
        while(true){
            delay(10000)
            currentUser = FirebaseAuth.getInstance().currentUser
        }
    }

    private suspend fun load(){
        while (true){
            delay(1000)
            val user = User()
            if (currentUser != null){
                RealtimeDatabase.readUser(currentUser!!.uid, user){
                    runBlocking {
                        for (i in 0 until user.groupsId.size) {
                            launch { loadGroup(user.groupsId[i]) }
                        }
                        ChangesFileHandler(mapOfChanges, context).save()
                    }
                }
            }
        }
    }

    private fun loadGroup(gId : String){
        val group = Group()
        RealtimeDatabase.readGroup(gId, group){
            for (change in group.changes){
                if (mapOfChanges[change.id] == null) {
                    val task = change.taskSnapshot.toTask()
                    taskList.addTask(task)
                    taskManager.getTaskListByDay(task.executionPeriod.startDate).addTask(task)
                    mapOfChanges[change.id] = true
                    onTasksAdded()
                    }
                }
        }
    }

}