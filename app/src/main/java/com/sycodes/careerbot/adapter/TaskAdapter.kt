package com.sycodes.careerbot.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sycodes.careerbot.R
import com.sycodes.careerbot.data.TaskEntity

class TaskAdapter(private var tasks: List<TaskEntity>) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TaskViewHolder {
        var inflate = LayoutInflater.from(parent.context).inflate(R.layout.itemtaskslayout, parent, false)
        return TaskViewHolder(inflate)
    }

    override fun onBindViewHolder(
        holder: TaskViewHolder,
        position: Int
    ) {
        val task =tasks[position]
        holder.titleTextView.text = task.title
        holder.summaryTextView.text = task.description
        holder.estimatedTimeTextView.text = "Estimated Time: ${task.estimatedHours}"
        holder.resourceTextView.text = "Resources: ${task.resources.joinToString(", ")}"

    }

    override fun getItemCount(): Int {
        return tasks.size   }

    class TaskViewHolder(view: View): RecyclerView.ViewHolder(view) {

        var titleTextView = view.findViewById<TextView>(R.id.taskTitleTextView)!!
        var summaryTextView = view.findViewById<TextView>(R.id.TaskSummaryTextView)!!
        var estimatedTimeTextView = view.findViewById<TextView>(R.id.taskEstimatedTimeTextView)!!
        var resourceTextView = view.findViewById<TextView>(R.id.taskResourceTextView)!!

    }
}