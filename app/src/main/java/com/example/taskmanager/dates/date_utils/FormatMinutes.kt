package com.example.taskmanager.utils.date_utils

fun formatMinutes(minutes: Int): String {
    return if (minutes >= 10) "$minutes"
    else "0$minutes"

}