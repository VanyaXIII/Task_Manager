package com.example.taskmanager.notifications

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_MIN
import com.example.taskmanager.MainActivity
import com.example.taskmanager.R
import com.example.taskmanager.tasks.Task
import com.example.taskmanager.tasks.TaskList
import com.example.taskmanager.tasks.TasksFileHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

class NotificationService : Service() {

    private lateinit var tasksQueue: ArrayDeque<Task>
    private lateinit var taskList: TaskList
    private var initialSize: Int = 0
    val context = this


    override fun onCreate() {
        super.onCreate()
        taskList = TaskList(TasksFileHandler(null, this).load(), true)
        tasksQueue = ArrayDeque(taskList.tasksByExTime)
        initialSize = tasksQueue.size
        updateTasksQueue()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        val channelId = createNotificationChannel(
            getString(R.string.notification_channel_id),
            getString(R.string.notification_channel_name)
        )

        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
        val notification = notificationBuilder.setOngoing(true)
            .setPriority(PRIORITY_MIN)
            .setContentTitle(getString(R.string.notification_title))
            .setContentText(getString(R.string.notification_message))
            .setContentIntent(pendingIntent)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()


        Thread {
            runBlocking {
                launch { checkForegroundNotification(notificationBuilder, pendingIntent) }
                launch { checkNewTasks() }
            }
        }.start()

        startForeground(getString(R.string.foreground_id).toInt(), notification)

        return START_STICKY
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String): String {
        val channel = NotificationChannel(
            channelId,
            channelName, NotificationManager.IMPORTANCE_NONE
        )
        channel.lightColor = Color.BLUE
        channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(channel)
        return channelId
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun updateTasksQueue() {
        if (tasksQueue.size == 0)
            return
        while (tasksQueue.first().executionPeriod.endDate < Calendar.getInstance())
            tasksQueue.removeFirst()
    }

    private fun getFirstUncompletedTask(): Task {
        while ((tasksQueue.size != 0) and (tasksQueue.first().isDone))
            tasksQueue.removeFirst()
        return tasksQueue.first()
    }

    private fun getForegroundNotification(
        notificationBuilder: NotificationCompat.Builder,
        pendingIntent: PendingIntent
    ): Notification {
        updateTasksQueue()
        val firstUncompletedTask = getFirstUncompletedTask()
        return notificationBuilder.setOngoing(true)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(PRIORITY_MIN)
            .setContentTitle("Информация о ближайшей задаче")
            .setContentText(firstUncompletedTask.executionPeriod.toString())
            .setContentIntent(pendingIntent)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
    }

    private fun notifyForeground(notification: Notification) {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(getString(R.string.foreground_id).toInt(), notification)
    }

    private suspend fun checkForegroundNotification(
        notificationBuilder: NotificationCompat.Builder,
        pendingIntent: PendingIntent
    ) {
        while (true) {
            delay(500)
            notifyForeground(getForegroundNotification(notificationBuilder, pendingIntent))
        }
    }

    private suspend fun checkNewTasks() {
        while (true) {
            delay(500)
            taskList = TaskList(TasksFileHandler(null, this).load(), true)
            if (taskList.tasks?.size ?: 0 != initialSize) {
                tasksQueue = ArrayDeque(taskList.tasksByExTime)
                updateTasksQueue()
            }
        }
    }

}
