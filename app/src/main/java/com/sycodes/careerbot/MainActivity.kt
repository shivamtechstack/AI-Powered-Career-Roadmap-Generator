package com.sycodes.careerbot

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.sycodes.careerbot.data.AppDatabase
import com.sycodes.careerbot.data.RoadmapAdapter
import com.sycodes.careerbot.data.RoadmapEntity
import com.sycodes.careerbot.data.SharedPreferencesHelper
import com.sycodes.careerbot.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        sharedPreferencesHelper = SharedPreferencesHelper(this)
        val name = sharedPreferencesHelper.getUserName()
        binding.mainActivityNameTextView.text = "Welcome, $name"

        binding.floatingActionButton.setOnClickListener {
            val intent = Intent(this, AddNewRoadmapActivity::class.java)
            startActivity(intent)
        }
        loadRoadmaps()
    }

    override fun onResume() {
        super.onResume()
        loadRoadmaps()
    }
    private fun loadRoadmaps() {
        val database = AppDatabase.getAppDatabase(this).roadmapDao()

        CoroutineScope(Dispatchers.IO).launch {
            val roadmaps = database.getAllRoadmaps()
            withContext(Dispatchers.Main) {
                binding.mainRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                binding.mainRecyclerView.adapter = RoadmapAdapter(roadmaps, onClickListener = {
                    val intent = Intent(this@MainActivity, TaskActivity::class.java)
                    intent.putExtra("roadmapId", it.id)
                    intent.putExtra("roadmapTitle", it.title)
                    intent.putExtra("roadmapSummary", it.summary)
                    intent.putExtra("roadmapEstimatedTime", it.estimatedTime)
                    startActivity(intent)
                }, onLongClickListener = { roadmap, view ->
                    showPopupMenu(view, roadmap)
                })
            }
        }
    }

    private fun showPopupMenu(anchorView: View, roadmap: RoadmapEntity) {
        val popupMenu = PopupMenu(this, anchorView)
        popupMenu.menuInflater.inflate(R.menu.roadmap_popup_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_delete -> {
                    deleteRoadmapAndTasks(roadmap)
                    true
                }
                else -> false
            }
        }

        popupMenu.show()
    }

    private fun deleteRoadmapAndTasks(roadmap: RoadmapEntity) {
        val roadmapDao = AppDatabase.getAppDatabase(this).roadmapDao()
        val taskDao = AppDatabase.getAppDatabase(this).taskDao()

        CoroutineScope(Dispatchers.IO).launch {
            taskDao.deleteTasksForRoadmap(roadmap.id.toInt())
            roadmapDao.deleteRoadmapById(roadmap.id.toInt())
            withContext(Dispatchers.Main) {
                loadRoadmaps()
                Toast.makeText(this@MainActivity, "Deleted \"${roadmap.title}\"", Toast.LENGTH_SHORT).show()
            }
        }
    }

}