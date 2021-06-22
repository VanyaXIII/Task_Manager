package com.example.taskmanager.ui.task_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanager.R
import com.example.taskmanager.tasks.Task
import com.example.taskmanager.tasks.TaskList
import com.example.taskmanager.tasks.TaskManager
import com.example.taskmanager.tasks.TasksFileHandler


class RecycleViewTasksAdapter(private val tasks: ArrayList<Task>, private val taskList: TaskList = TaskList(),
                              var taskManager: TaskManager = TaskManager()
) :
    RecyclerView.Adapter<RecycleViewTasksAdapter.ViewHolder>() {


    var onTaskDeleteListener : (Task) -> Unit = {}

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var descriptionView: TextView? = null
        var executionView : TextView? = null
        var executionImage : ImageView? = null
        var deleteImage : ImageView? = null
        val view = itemView
        init {
            descriptionView = itemView.findViewById(R.id.description)
            executionView = itemView.findViewById(R.id.executionPeriod)
            executionImage = itemView.findViewById(R.id.imageView)
            deleteImage = itemView.findViewById(R.id.deleteImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recyclerview_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = tasks[position]
        holder.descriptionView?.text = task.description
        holder.executionView?.text = task.executionPeriod.toString()
        if (!task.isDone) {
            holder.executionImage?.setImageResource(R.drawable.not_completed)
        }
        holder.executionImage?.setOnClickListener {
            task.isDone = !task.isDone
            if (task.isDone)
                holder.executionImage?.setImageResource(R.drawable.completed)
                            
            else
                holder.executionImage?.setImageResource(R.drawable.not_completed)
            TasksFileHandler(taskList, holder.view.context).save()
        }
        holder.deleteImage?.setOnClickListener {
            val index = tasks.indexOf(task)
            this.notifyItemRemoved(index)
            tasks.remove(task)
            taskList.tasks?.remove(task)
            taskManager.deleteTask(task)
            TasksFileHandler(taskList, holder.view.context).save()
        }
    }

    override fun getItemCount() = tasks.size
}