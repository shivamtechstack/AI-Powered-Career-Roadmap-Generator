package com.sycodes.careerbot.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sycodes.careerbot.R
import com.sycodes.careerbot.data.AppDatabase
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

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: TaskViewHolder,
        position: Int
    ) {
        val task =tasks[position]
        holder.titleTextView.text = task.title
        holder.summaryTextView.text = task.description
        holder.estimatedTimeTextView.text = "Estimated Time: ${task.estimatedHours}"
        holder.taskNumberTextView.text = "Task ${position + 1}"

        if (task.isCompleted){
            holder.checkBox.isChecked = true
        }else{
            holder.checkBox.isChecked = false
        }

        holder.linkPreviewContainer.removeAllViews()

        val urls = task.resources.map { it.trim() }
            .filter { it.isNotEmpty() && Patterns.WEB_URL.matcher(it).matches() && (it.startsWith("http://") || it.startsWith("https://")) }

        for (url in urls) {
            val previewView = LayoutInflater.from(holder.itemView.context)
                .inflate(R.layout.link_preview_item, holder.linkPreviewContainer, false)

            val titleView = previewView.findViewById<TextView>(R.id.linkPreviewTitle)
            val domainView = previewView.findViewById<TextView>(R.id.linkPreviewDomain)
            val imageView = previewView.findViewById<ImageView>(R.id.linkPreviewImage)
            val cardView = previewView.findViewById<CardView>(R.id.linkPreviewCard)

            cardView.setOnClickListener {
                try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    it.context.startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(it.context, "Invalid URL", Toast.LENGTH_SHORT).show()
                }
            }

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val doc = Jsoup.connect(url).get()
                    val ogTitle = doc.select("meta[property=og:title]").attr("content")
                    val ogImage = doc.select("meta[property=og:image]").attr("content")
                    val ogUrl = doc.select("meta[property=og:url]").attr("content")
                    val domain = Uri.parse(ogUrl.ifEmpty { url }).host

                    withContext(Dispatchers.Main) {
                        titleView.text = ogTitle.ifEmpty { url }
                        domainView.text = domain ?: ""
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
        var checkBox = view.findViewById<CheckBox>(R.id.taskCheckbox)!!
        val linkPreviewContainer: LinearLayout = view.findViewById(R.id.linkPreviewContainer)

    }
}