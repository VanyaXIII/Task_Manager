package com.example.taskmanager.ui.task_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanager.R
import com.example.taskmanager.tasks.Task


class RecycleViewTasksAdapter(private val tasks: List<Task>) :
    RecyclerView.Adapter<RecycleViewTasksAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var descriptionView: TextView? = null
        var executionView : TextView? = null
        var imageView : ImageView? = null
        init {
            descriptionView = itemView.findViewById(R.id.description)
            executionView = itemView.findViewById(R.id.executionPeriod)
            imageView = itemView.findViewById(R.id.imageView)
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
            holder.imageView?.setImageResource(R.drawable.not_completed)
        }
        holder.imageView?.setOnClickListener {
            task.isDone = !task.isDone
            if (task.isDone)
                holder.imageView?.setImageResource(R.drawable.completed)
            else
                holder.imageView?.setImageResource(R.drawable.not_completed)
        }
    }

    override fun getItemCount() = tasks.size
}