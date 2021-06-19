package com.example.taskmanager.utils

import android.app.Application
import android.content.Intent
import com.example.taskmanager.notifications.NotificationService

class ServiceStarter : Application() {

    override fun onCreate() {
        super.onCreate()
        startNotificationService()
    }

    private fun startNotificationService(){
        Thread {
            val serviceIntent = Intent(this, NotificationService::class.java)
            startService(serviceIntent)
        }.start()
    }

}