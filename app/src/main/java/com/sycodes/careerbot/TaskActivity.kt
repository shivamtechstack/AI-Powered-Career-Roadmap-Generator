package com.sycodes.careerbot

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.sycodes.careerbot.adapter.TaskAdapter
import com.sycodes.careerbot.data.AppDatabase
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
                var taskList = database.getTasksForRoadmap(roadmapId)
                withContext(Dispatchers.Main){
                    binding.tasksRecyclerView.layoutManager = LinearLayoutManager(this@TaskActivity)
                    binding.tasksRecyclerView.adapter = TaskAdapter(taskList)

                }
            }
    }
}