package com.example.taskmanager.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanager.R
import com.example.taskmanager.tasks.Task

class RecycleViewTasksAdapter(private val tasks: List<Task>) :
    RecyclerView.Adapter<RecycleViewTasksAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var descriptionView: TextView? = null
        var executionView : TextView? = null
        init {
            descriptionView = itemView.findViewById(R.id.description)
            executionView = itemView.findViewById(R.id.executionPeriod)
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
    }

    override fun getItemCount() = tasks.size
}