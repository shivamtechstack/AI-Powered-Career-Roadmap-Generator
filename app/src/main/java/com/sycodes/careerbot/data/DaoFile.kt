package com.sycodes.careerbot.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RoadmapDao {
    @Insert
    suspend fun insertRoadmap(roadmap: RoadmapEntity): Long

    @Query("SELECT * FROM roadmaps")
    suspend fun getAllRoadmaps(): List<RoadmapEntity>

    @Query("SELECT * FROM roadmaps WHERE id = :id")
    suspend fun getRoadmapById(id: Int): RoadmapEntity

    @Query("DELETE FROM roadmaps WHERE id = :id")
    suspend fun deleteRoadmapById(id: Int)

}

@Dao
interface TaskDao {
    @Insert
    suspend fun insertTask(task: TaskEntity)

    @Query("SELECT * FROM tasks WHERE roadmapId = :roadmapId")
    suspend fun getTasksForRoadmap(roadmapId: Long): List<TaskEntity>

    @Query("DELETE FROM tasks WHERE roadmapId = :roadmapId")
    suspend fun deleteTasksForRoadmap(roadmapId: Int)

//    @Query("UPDATE tasks SET isCompleted = :isCompleted WHERE id = :taskId")
//    suspend fun updateTaskStatus(taskId: Int, isCompleted: Boolean)
}
