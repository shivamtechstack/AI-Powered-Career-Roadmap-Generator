package com.sycodes.careerbot

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.sycodes.careerbot.adapter.TaskAdapter
import com.sycodes.careerbot.data.AppDatabase
import com.sycodes.careerbot.data.TaskDao
import com.sycodes.careerbot.databinding.ActivityTaskBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TaskActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTaskBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        var roadmapId = intent.getLongExtra("roadmapId", 0L)
        var roadmapTitle = intent.getStringExtra("roadmapTitle")
        var roadmapSummary = intent.getStringExtra("roadmapSummary")
        var roadmapEstimatedTime = intent.getStringExtra("roadmapEstimatedTime")

        binding.taskRoadmapTitleTextView.text = roadmapTitle
        binding.taskRoadmapSummaryTextView.text = roadmapSummary
        binding.taskRoadmapEstimatedTimeTextView.text = "Time Commitment $roadmapEstimatedTime"


        var database = AppDatabase.getAppDatabase(this).taskDao()

        CoroutineScope(Dispatchers.IO).launch {
            val list = database.getTasksForRoadmap(roadmapId).toMutableList()
            updateProgress(list.count { it.isCompleted }, list.size)

            var taskList = database.getTasksForRoadmap(roadmapId)
            withContext(Dispatchers.Main) {
                binding.tasksRecyclerView.layoutManager = LinearLayoutManager(this@TaskActivity)
                binding.tasksRecyclerView.adapter = TaskAdapter(taskList, onStatusChanged = { completed, total ->
                    updateProgress(completed,total)
                })

            }
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (isTaskRoot) {
                    val intent = Intent(this@TaskActivity, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                } else {
                    finish()
                }
            }
        })
    }
    private fun updateProgress(completed: Int, total: Int) {
        val percent = if (total > 0) (completed * 100 / total) else 0
        binding.taskProgressBar.progress = percent
        binding.progressTextView.text = "Progress: $percent%"
    }

}