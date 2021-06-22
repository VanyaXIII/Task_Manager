package com.example.taskmanager.ui.task_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanager.R
import com.example.taskmanager.tasks.TaskList
import com.example.taskmanager.tasks.TaskManager
import java.util.*

class ViewPagerAdapter(private val taskManager: TaskManager, private val taskList: TaskList) :
    RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>() {

    companion object {
        var choosingParams = ChoosingParams.SIMPLE
        var date: Calendar = initDate()


        private fun initDate() :Calendar{
            val d = Calendar.getInstance()
            d.add(Calendar.DAY_OF_YEAR, -50)
            return d
        }

    }



    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var recyclerView: RecyclerView? = null

        init {
            recyclerView = itemView.findViewById(R.id.rv)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.viewpager_item, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.itemView.run {
        val recyclerView = findViewById<RecyclerView>(R.id.rv)
        val newDate = date.clone() as Calendar
        newDate.add(Calendar.DAY_OF_YEAR, position)
        when (choosingParams) {
            ChoosingParams.SIMPLE -> recyclerView.adapter =
                RecycleViewTasksAdapter(ArrayList(taskManager.getTaskListByDay(newDate).tasks), taskList, taskManager)
            ChoosingParams.BY_DATE ->
                recyclerView.adapter = RecycleViewTasksAdapter(taskManager.getTaskListByDay(newDate).tasksByDate, taskList, taskManager)
            ChoosingParams.BY_EX_TIME -> recyclerView.adapter =
                RecycleViewTasksAdapter(taskManager.getTaskListByDay(newDate).tasksByExTime, taskList, taskManager)
        }
        recyclerView.layoutManager = LinearLayoutManager(context)
    }


    override fun getItemCount(): Int = 100
}

enum class ChoosingParams {

    SIMPLE, BY_DATE, BY_EX_TIME

}


