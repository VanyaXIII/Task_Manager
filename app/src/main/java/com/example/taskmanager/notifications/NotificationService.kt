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
import androidx.core.app.NotificationManagerCompat
import com.example.taskmanager.MainActivity
import com.example.taskmanager.R
import com.example.taskmanager.tasks.Task
import com.example.taskmanager.tasks.TaskList
import com.example.taskmanager.tasks.TasksFileHandler
import com.example.taskmanager.users.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.collections.HashSet

class NotificationService : Service() {

    private lateinit var tasksQueue: ArrayDeque<Task>
    private lateinit var taskList: TaskList
    private var currentSizeOfTaskList: Int = 0
    private val uid = FirebaseAuth.getInstance().currentUser!!.uid


    override fun onCreate() {
        super.onCreate()
        taskList = TaskList(TasksFileHandler(null, this).load(uid), true)
        tasksQueue = ArrayDeque(taskList.tasksByExTime)
        currentSizeOfTaskList = tasksQueue.size
        updateTasksQueue()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        val foregroundChannelId = createNotificationChannel(
            getString(R.string.foreground_notification_channel_id),
            getString(R.string.foreground_notification_channel_name)
        )

        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)
        val foregroundNotificationBuilder = NotificationCompat.Builder(this, foregroundChannelId)
        val foregroundNotification =
            getForegroundNotification(foregroundNotificationBuilder, pendingIntent)


        val taskChannelId = createNotificationChannel(
            getString(R.string.task_notification_channel_id),
            getString(R.string.task_notification_channel_name)
        )

        val taskNotificationBuilder = NotificationCompat.Builder(this, taskChannelId)


        Thread {
            runBlocking {
                launch { checkForegroundNotification(foregroundNotificationBuilder, pendingIntent) }
                launch { startTasksUpdating() }
                launch { checkTaskNotification(taskNotificationBuilder, pendingIntent) }
            }
        }.start()

        startForeground(getString(R.string.foreground_id).toInt(), foregroundNotification)

        return START_STICKY
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String): String {
        val channel = NotificationChannel(
            channelId,
            channelName, NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.lightColor = Color.BLUE
        channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
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
        while ((tasksQueue.size != 0) and (tasksQueue.first().executionPeriod.endDate < Calendar.getInstance()))
            tasksQueue.removeFirst()
    }

    private fun getFirstUncompletedTask(): Task? {
        if (tasksQueue.size == 0)
            return null
        while (tasksQueue.first().isDone) {
            tasksQueue.removeFirst()
            if (tasksQueue.size == 0)
                break
        }
        if (tasksQueue.size == 0)
            return null
        while (tasksQueue.first().executionPeriod.endDate < Calendar.getInstance()) {
            tasksQueue.removeFirst()
            if (tasksQueue.size == 0)
                break
        }
        return tasksQueue.first()
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

    private suspend fun checkTaskNotification(
        notificationBuilder: NotificationCompat.Builder,
        pendingIntent: PendingIntent
    ) {
        while (true) {
            delay(1000L)
            var doSaving = false
            loadNewTasks()
            for (task in taskList.tasks ?: HashSet()) {
                if ((task.notificationParams.notificationTime < Calendar.getInstance()) and (!task.notificationParams.notified)
                    and (task.executionPeriod.endDate > Calendar.getInstance())
                ) {
                    doSaving = true
                    task.notificationParams.notified = true
                    sendTaskNotification(
                        getTaskNotification(
                            task,
                            notificationBuilder,
                            pendingIntent
                        )
                    )
                }
            }
            if (doSaving)
                TasksFileHandler(taskList, this).save()
        }
    }

    private suspend fun startTasksUpdating() {
        while (true) {
            delay(500)
            loadNewTasks()
        }
    }

    private fun loadNewTasks() {
        val tl = TaskList(TasksFileHandler(null, this).load(FirebaseAuth.getInstance().currentUser!!.uid))
        if (tl.tasks?.size ?: 0 != currentSizeOfTaskList) {
            taskList = tl
            currentSizeOfTaskList = taskList.tasks?.size ?: 0
            taskList.doSorting = true
            taskList.updateSortedLists()
            tasksQueue = ArrayDeque(taskList.tasksByExTime)
            updateTasksQueue()
        }
    }

    private fun sendTaskNotification(notification: Notification) {
        with(NotificationManagerCompat.from(this)) {
            notify(getString(R.string.notification_id).toInt(), notification)
        }
    }

    private fun getForegroundNotification(
        notificationBuilder: NotificationCompat.Builder,
        pendingIntent: PendingIntent
    ): Notification {
        updateTasksQueue()
        val firstUncompletedTask = getFirstUncompletedTask()
        val title =
            getString(
                R.string.foreground_notification_title,
                firstUncompletedTask?.description ?: "Задач нет"
            )
        val text =
            getString(
                R.string.foreground_notification_text,
                firstUncompletedTask?.executionPeriod ?: "Задач нет"
            )
        return notificationBuilder.setOngoing(true)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(PRIORITY_MIN)
            .setContentTitle(title)
            .setContentText(text)
            .setContentIntent(pendingIntent)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
    }

    private fun getTaskNotification(
        task: Task,
        notificationBuilder: NotificationCompat.Builder,
        pendingIntent: PendingIntent
    ): Notification {
        val title =
            getString(R.string.task_notification_title, task.description)
        val text =
            getString(R.string.task_notification_text, task.getTimeUntilEnding()[Calendar.MINUTE])
        return notificationBuilder.setOngoing(true)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(PRIORITY_MIN)
            .setContentTitle(title)
            .setContentText(text)
            .setContentIntent(pendingIntent)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
    }
}
