package com.example.taskmanager.tasks

import android.content.Context
import android.util.Log
import com.example.taskmanager.R
import com.example.taskmanager.users.User
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class TasksFileHandler(private val taskList: TaskList?, private val context: Context) {

    fun save() {
        val jsonString = taskList?.getTasksAsJson()
        val outputStream = context.openFileOutput(
            context.getString(R.string.path_to_json_task_list),
            Context.MODE_PRIVATE
        )
        outputStream.write(jsonString?.toByteArray())
        outputStream.close()
    }

    fun add(task: Task){
        val dataBase = FirebaseDatabase.getInstance().getReference("Tasks")
        dataBase.push().setValue(task)
        val dataBaseUsers = FirebaseDatabase.getInstance().getReference("Users")
        dataBaseUsers.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (i in snapshot.children){
                    val user = i.getValue(User::class.java)
                    if(task.ids.contains(user!!.getUid().toString())){
                        user.tasks.add(task)
                    }
                    dataBaseUsers.child(user.getUid().toString()).setValue(user)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("cancel", error.toString())
            }

        })
    }

    fun load(uid: String): HashSet<Task> {
        val dataBase = FirebaseDatabase.getInstance().getReference("Users").child(uid)
        var res: HashSet<Task> = HashSet()
        dataBase.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (i in snapshot.getValue(User::class.java)!!.tasks){
                    res.add(i)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("cancel", error.toString())
            }


        })
        return res


//        return try {
//            val inputStream =
//                context.openFileInput(context.getString(R.string.path_to_json_task_list))
//            val bytes = ByteArray(inputStream.available())
//            inputStream.read(bytes)
//            inputStream.close()
//            val jsonString = String(bytes)
//            val mapper = jacksonObjectMapper()
//            return mapper.readValue(jsonString)
//        } catch (e: Exception) {
//            TasksFileHandler(TaskList(HashSet()), context, currentUser)
//            HashSet()
//        }
    }
}