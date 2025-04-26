package com.sycodes.careerbot.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "roadmaps")
data class RoadmapEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val summary: String,
    val estimatedTime: String,
    val createdAt: Long
)

@Entity(
    tableName = "tasks",
    foreignKeys = [ForeignKey(
        entity = RoadmapEntity::class,
        parentColumns = ["id"],
        childColumns = ["roadmapId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val roadmapId: Long,
    val title: String,
    val description: String,
    val isCompleted: Boolean,
    val estimatedHours: Int,
    val resources: List<String>
)

data class RoadmapResponse(
    val title: String,
    val summary: String,
    val estimatedTimeToComplete: String,
    val tasks: List<TaskResponse>
)

data class TaskResponse(
    val title: String,
    val description: String,
    val estimatedHours: Int,
    val resources: List<String>
)

