package com.sycodes.careerbot.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sycodes.careerbot.R
import com.sycodes.careerbot.utility.DateAndTimeFormat

class RoadmapAdapter(private var roadmaps: List<RoadmapEntity>, private var onClickListener: (RoadmapEntity) -> Unit, private var onLongClickListener: (RoadmapEntity) -> Unit) : RecyclerView.Adapter<RoadmapAdapter.RoadmapViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RoadmapViewHolder {
        var inflate = LayoutInflater.from(parent.context).inflate(R.layout.itemroadmaplayout, parent, false)
        return RoadmapViewHolder(inflate)
    }

    override fun onBindViewHolder(
        holder: RoadmapViewHolder,
        position: Int
    ) {
        val roadmap = roadmaps[position]
        holder.titleTextView.text = roadmaps[position].title
        holder.summaryTextView.text = roadmaps[position].summary
        holder.creationTimeTextView.text = "Created on ${DateAndTimeFormat.formatTimestamp(roadmaps[position].createdAt)}"
        holder.estimatedTimeTextView.text = "Time: ${roadmaps[position].estimatedTime}"

        holder.itemView.setOnClickListener {
            onClickListener(roadmap)
        }

        holder.itemView.setOnLongClickListener {
            onLongClickListener(roadmap)
            true
        }
    }

    override fun getItemCount(): Int {
        return roadmaps.size
    }

    class RoadmapViewHolder(view: View): RecyclerView.ViewHolder(view) {

        var titleTextView = view.findViewById<TextView>(R.id.roadmapTitleTextView)!!
        var summaryTextView = view.findViewById<TextView>(R.id.roadmapSummaryTextView)!!
        var creationTimeTextView = view.findViewById<TextView>(R.id.roadmapCreationTimeTextView)!!
        var estimatedTimeTextView = view.findViewById<TextView>(R.id.roadmapEstimatedTimeTextView)!!

    }
}