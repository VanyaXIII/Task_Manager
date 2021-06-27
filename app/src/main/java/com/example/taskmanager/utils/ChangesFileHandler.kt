package com.example.taskmanager.utils

import android.content.Context
import com.example.taskmanager.R
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

class ChangesFileHandler(private val mapOfChanges : HashMap<String, Boolean>? = HashMap(), private val context: Context) {

    fun save() {
        val mapper = jacksonObjectMapper()
        val jsonString = mapper.writeValueAsString(mapOfChanges)
        val outputStream = context.openFileOutput("changes.json",
            Context.MODE_PRIVATE
        )
        outputStream.write(jsonString?.toByteArray())
        outputStream.close()
    }

    fun load(): HashMap<String, Boolean> {
        return try {
            val inputStream =
                context.openFileInput("changes.json")
            val bytes = ByteArray(inputStream.available())
            inputStream.read(bytes)
            inputStream.close()
            val jsonString = String(bytes)
            val mapper = jacksonObjectMapper()
            return mapper.readValue(jsonString)
        } catch (e: Exception) {
            HashMap()
        }
    }

}