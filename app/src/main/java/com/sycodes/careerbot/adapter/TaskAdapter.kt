package com.sycodes.careerbot.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sycodes.careerbot.R
import com.sycodes.careerbot.data.TaskEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

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
        holder.taskNumberTextView.text = "Task ${position + 1}"

        holder.linkPreviewContainer.removeAllViews()

        val urls = task.resources.map { it.trim() }.filter { it.isNotEmpty() }

        for (url in urls) {
            val previewView = LayoutInflater.from(holder.itemView.context)
                .inflate(R.layout.link_preview_item, holder.linkPreviewContainer, false)

            val titleView = previewView.findViewById<TextView>(R.id.linkPreviewTitle)
            val domainView = previewView.findViewById<TextView>(R.id.linkPreviewDomain)
            val imageView = previewView.findViewById<ImageView>(R.id.linkPreviewImage)
            val cardView = previewView.findViewById<CardView>(R.id.linkPreviewCard)

            cardView.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                holder.itemView.context.startActivity(intent)
            }

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val doc = Jsoup.connect(url).get()
                    val ogTitle = doc.select("meta[property=og:title]").attr("content")
                    val ogImage = doc.select("meta[property=og:image]").attr("content")
                    val ogUrl = doc.select("meta[property=og:url]").attr("content")
                    val domain = Uri.parse(ogUrl).host ?: Uri.parse(url).host

                    withContext(Dispatchers.Main) {
                        titleView.text = ogTitle
                        domainView.text = domain
                        Glide.with(holder.itemView.context)
                            .load(ogImage)
                            .placeholder(R.drawable.ic_launcher_background)
                            .into(imageView)
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        titleView.text = url
                        domainView.text = ""
                        imageView.setImageResource(R.drawable.ic_launcher_foreground)
                        e.printStackTrace()
                    }
                }
            }
            holder.linkPreviewContainer.addView(previewView)
        }

    }

    override fun getItemCount(): Int {
        return tasks.size   }

    class TaskViewHolder(view: View): RecyclerView.ViewHolder(view) {

        var titleTextView = view.findViewById<TextView>(R.id.taskTitleTextView)!!
        var summaryTextView = view.findViewById<TextView>(R.id.TaskSummaryTextView)!!
        var estimatedTimeTextView = view.findViewById<TextView>(R.id.taskEstimatedTimeTextView)!!
        var taskNumberTextView = view.findViewById<TextView>(R.id.taskNumberTextView)!!

        val linkPreviewContainer: LinearLayout = view.findViewById(R.id.linkPreviewContainer)

    }
}