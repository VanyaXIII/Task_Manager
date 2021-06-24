package com.example.taskmanager.utils

import android.content.Context
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

class TimeStamp {

    companion object{

        fun getTimeStamp(context : Context) : Long{
            return try {
                val inputStream =
                    context.openFileInput("time_stamp.json")
                val bytes = ByteArray(inputStream.available())
                inputStream.read(bytes)
                inputStream.close()
                val jsonString = String(bytes)
                val mapper = jacksonObjectMapper()
                return mapper.readValue(jsonString)
            } catch (e: Exception) {
                0
            }
        }

        fun setTimeStamp(timeStamp : Long, context: Context){
            val mapper = jacksonObjectMapper()
            val jsonString = mapper.writeValueAsString(timeStamp)
            val outputStream = context.openFileOutput(
                "time_stamp.json",
                Context.MODE_PRIVATE
            )
            outputStream.write(jsonString?.toByteArray())
            outputStream.close()
        }

    }


}