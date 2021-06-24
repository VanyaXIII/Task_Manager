package com.example.taskmanager.ui.authorization

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanager.GroupTaskCreatingActivity
import com.example.taskmanager.R
import com.example.taskmanager.users.Group

class RecycleViewGroupAdapter(private val groups : ArrayList<Group> = ArrayList()) : RecyclerView.Adapter<RecycleViewGroupAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var groupName: TextView? = null
        var addGroupTask : ImageView? = null
        val view = itemView
        init {
            groupName = itemView.findViewById(R.id.groupName)
            addGroupTask = itemView.findViewById(R.id.plusGroupTask)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recyclerview_group_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.groupName?.text = groups[position].name
        holder.addGroupTask?.setOnClickListener {
            GroupTaskCreatingActivity.group = groups[position]
            holder.itemView.context.startActivity(Intent(holder.itemView.context, GroupTaskCreatingActivity::class.java))
        }
    }

    override fun getItemCount(): Int = groups.size


}