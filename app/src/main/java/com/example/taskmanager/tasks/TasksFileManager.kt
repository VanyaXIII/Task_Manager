package com.example.taskmanager.tasks

import android.content.Context
import com.example.taskmanager.R
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

class TasksFileManager(private val taskList: TaskList?, private val context: Context) {

    fun save() {
        val jsonString = taskList?.getTasksAsJson()
        val outputStream = context.openFileOutput(context.getString(R.string.path_to_json_task_list), Context.MODE_PRIVATE)
        outputStream.write(jsonString?.toByteArray())
        outputStream.close()
    }

    fun load(): ArrayList<Task> {
        return try {

            val inputStream =
                context.openFileInput(context.getString(R.string.path_to_json_task_list))
            val bytes = ByteArray(inputStream.available())
            inputStream.read(bytes)
            inputStream.close()
            val jsonString = String(bytes)
            val mapper = jacksonObjectMapper()
            mapper.readValue(jsonString)
        } catch (e : Exception){
            val emptyArray = ArrayList<Task>()
            TasksFileManager(TaskList(emptyArray), context)
            emptyArray
        }
    }
}